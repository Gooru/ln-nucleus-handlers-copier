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

class CopyAssessmentHandler implements DBHandler {
    private final ProcessorContext context;
    private AJEntityCollection assessment;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyAssessmentHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

    public CopyAssessmentHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy assessment");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.assessmentId())) {
            LOGGER.error("Invalid request, source assessment id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP004)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // Fetch the content where type is assessment and it is not deleted already and id is specified id
        this.assessment = AJEntityCollection
            .findFirst(AJEntityCollection.AUTHORIZER_QUERY, AJEntityCollection.ASSESSEMENT, this.context.assessmentId(),
                false);
        // Assessment should be present in DB
        if (this.assessment == null) {
            LOGGER.warn("Assessment id: {} not present in DB", context.assessmentId());
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP014)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return AuthorizerBuilder.buildCopyAssessmentAuthorizer(this.context).authorize(this.assessment);

    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final UUID userId = UUID.fromString(context.userId());
        final UUID assessmentId = UUID.fromString(context.assessmentId());
        Object copyAssessmentId = Base.firstCell(AJEntityCollection.COPY_COLLECTION_QUERY, assessmentId, AJEntityCollection.ASSESSEMENT, userId, context.tenant(), context.tenantRoot());
        if (copyAssessmentId != null) {
          return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyAssessmentId.toString(),
              EventBuilderFactory.getCopyAssessmentEventBuilder(copyAssessmentId.toString())),
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
