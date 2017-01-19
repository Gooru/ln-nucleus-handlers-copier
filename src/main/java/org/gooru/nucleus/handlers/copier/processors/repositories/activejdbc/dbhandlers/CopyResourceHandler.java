package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityOriginalResource;
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
    private AJEntityContent sourceContent;
    private AJEntityOriginalResource sourceOriginalResource;

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
        // Check the resource in original table first, if not then fall back on content table
        this.sourceOriginalResource =
            AJEntityOriginalResource.findFirst(AJEntityOriginalResource.AUTHORIZER_QUERY, this.context.resourceId(),
                false);
        if (this.sourceOriginalResource == null) {
            this.sourceContent = AJEntityContent
                .findFirst(AJEntityContent.AUTHORIZER_QUERY, AJEntityContent.RESOURCE, this.context.resourceId(), false);
            if (this.sourceContent == null) {
                LOGGER.warn("Resource id: {} not present in DB", context.resourceId());
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP011)),
                    ExecutionResult.ExecutionStatus.FAILED);
            }
            LOGGER.info("copying resource reference '{}'", this.context.resourceId());
            return AuthorizerBuilder.buildCopyResourceRefAuthorizer(this.context).authorize(this.sourceContent);
        } else {
            LOGGER.info("copying original resource '{}'", this.context.resourceId());
            return AuthorizerBuilder.buildCopyResourceAuthorizer(this.context).authorize(this.sourceOriginalResource);
        }


    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final String newResourceId = UUID.randomUUID().toString();
        final UUID userId = UUID.fromString(this.context.userId());
        final UUID resourceId = UUID.fromString(this.context.resourceId());
        String title = this.context.request().getString("title");
        int count = 0;

        if (this.sourceOriginalResource != null) {
            count = Base.exec(AJEntityContent.COPY_ORIGINAL_RESOURCE_QUERY, UUID.fromString(newResourceId),
                context.tenant(), context.tenantRoot(), userId, userId, resourceId, resourceId, resourceId);
        } else {
            final UUID originalContentId =
                UUID.fromString(this.sourceContent.getString(AJEntityContent.ORIGINAL_CONTENT_ID));
            final UUID originalCreatorId =
                UUID.fromString(this.sourceContent.getString(AJEntityContent.ORIGINAL_CREATOR_ID));
            title = title != null && !title.isEmpty() ? title : this.sourceContent.getString(AJEntityContent.TITLE);
            count = Base.exec(AJEntityContent.COPY_REFERENCE_RESOURCE_QUERY, UUID.fromString(newResourceId),
                context.tenant(), context.tenantRoot(), title, userId, userId, originalCreatorId, originalContentId,
                resourceId, resourceId);
        }
        if (count == 0) {
            return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(MessageResponseFactory
            .createCreatedResponse(newResourceId, EventBuilderFactory.getCopyResourceEventBuilder(newResourceId)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

}
