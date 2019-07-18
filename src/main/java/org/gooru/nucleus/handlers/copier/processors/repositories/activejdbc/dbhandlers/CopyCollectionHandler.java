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

class CopyCollectionHandler implements DBHandler {
    private final ProcessorContext context;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyCollectionHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private AJEntityCollection collection;

    public CopyCollectionHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy collection");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.collectionId())) {
            LOGGER.error("Invalid request, source collection id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP003)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // Fetch the content where type is collection and it is not deleted already and id is specified id

        this.collection = AJEntityCollection
            .findFirst(AJEntityCollection.AUTHORIZER_QUERY, AJEntityCollection.COLLECTION, this.context.collectionId(),
                false);
        // Collection should be present in DB
        if (this.collection == null) {
            LOGGER.warn("Collection id: {} not present in DB", context.collectionId());
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP013)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return AuthorizerBuilder.buildCopyCollectionAuthorizer(this.context).authorize(this.collection);

    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final UUID userId = UUID.fromString(context.userId());
        final UUID collectionId = UUID.fromString(context.collectionId());
        Object copyCollectionId = Base.firstCell(AJEntityCollection.COPY_COLLECTION_QUERY, collectionId, AJEntityCollection.COLLECTION, userId, context.tenant(), context.tenantRoot());
        if (copyCollectionId != null) {
          return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyCollectionId.toString(),
              EventBuilderFactory.getCopyCollectionEventBuilder(copyCollectionId.toString())),
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
