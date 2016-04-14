package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyResourceHandler implements DBHandler {
    private final ProcessorContext context;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyResourceHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private AJEntityContent targetContent;

    public CopyResourceHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy resource.");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.resourceId())) {
            LOGGER.error("Invalid request, source resource id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP001)),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // Fetch the content where type is resource and it is not deleted
        // already
        // and id is specified id
        LazyList<AJEntityContent> resources = AJEntityContent.where(AJEntityContent.AUTHORIZER_QUERY,
            AJEntityContent.RESOURCE, this.context.resourceId(), false);
        // Resource should be present in DB
        if (resources.size() < 1) {
            LOGGER.warn("Resource id: {} not present in DB", context.resourceId());
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP011)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        this.targetContent = resources.get(0);
        return AuthorizerBuilder.buildCopyResourceAuthorizer(this.context).authorize(targetContent);

    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final String resourceId = UUID.randomUUID().toString();
        final UUID userId = UUID.fromString(this.context.userId());
        final UUID parentResourceId = UUID.fromString(this.context.resourceId());
        int count = Base.exec(AJEntityContent.COPY_RESOURCE_QUERY, UUID.fromString(resourceId), userId, userId,
            parentResourceId, parentResourceId, parentResourceId);
        if (count == 0) {
            return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(
            MessageResponseFactory.createCreatedResponse(resourceId,
                EventBuilderFactory.getCopyResourceEventBuilder(resourceId)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

}
