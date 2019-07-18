package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;
import java.util.UUID;
import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyOfflineActivityHandler implements DBHandler {
    private final ProcessorContext context;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyOfflineActivityHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private AJEntityCollection offlineActivity;

    public CopyOfflineActivityHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy offline activity");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.offlineActivityId())) {
            LOGGER.error("Invalid request, source offline activity id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP025)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // Fetch the content where type is offline activity and it is not deleted already and id is specified id

        this.offlineActivity = AJEntityCollection
            .findFirst(AJEntityCollection.AUTHORIZER_QUERY, AJEntityCollection.OFFLINE_ACTIVITY, this.context.offlineActivityId(),
                false);
        // Offline Activity should be present in DB
        if (this.offlineActivity == null) {
            LOGGER.warn("Offline Activity id: {} not present in DB", context.offlineActivityId());
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP026)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return AuthorizerBuilder.buildCopyOfflineActivityAuthorizer(this.context).authorize(this.offlineActivity);

    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
      final UUID userId = UUID.fromString(context.userId());
      final UUID offlineActivityId = UUID.fromString(context.offlineActivityId());
      Object copyOfflineActivityId = Base.firstCell(AJEntityCollection.COPY_COLLECTION_QUERY, offlineActivityId, AJEntityCollection.OFFLINE_ACTIVITY, userId, context.tenant(), context.tenantRoot());
      if (copyOfflineActivityId != null) {
        return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyOfflineActivityId.toString(),
            EventBuilderFactory.getCopyOfflineActivityEventBuilder(copyOfflineActivityId.toString())),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
      }

      return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
          ExecutionResult.ExecutionStatus.FAILED);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }
}
