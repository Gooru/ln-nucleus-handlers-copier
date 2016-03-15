package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class CopyCollectionHandler implements DBHandler {
  private final ProcessorContext context;
  private AJEntityCollection collection;

  private static final Logger LOGGER = LoggerFactory.getLogger(CopyCollectionHandler.class);

  public CopyCollectionHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    // There should be an question id present
    if (context.collectionId() == null || context.collectionId().isEmpty()) {
      LOGGER.warn("Missing collection id");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    // The user should not be anonymous
    if (context.userId() == null || context.userId().isEmpty() || context.userId().equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)) {
      LOGGER.warn("Anonymous user attempting to copy collection");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    // Fetch the content where type is collection and it is not deleted already
    // and id is specified id

    LazyList<AJEntityCollection> collections =
            AJEntityCollection.where(AJEntityCollection.AUTHORIZER_QUERY, AJEntityCollection.COLLECTION, this.context.collectionId(), false);
    // Question should be present in DB
    if (collections.size() < 1) {
      LOGGER.warn("Collection id: {} not present in DB", context.collectionId());
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    this.collection = collections.get(0);
    return AuthorizerBuilder.buildCopyCollectionAuthorizer(this.context).authorize(collection);

  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final String copyCollectionId = UUID.randomUUID().toString();
    final UUID userId = UUID.fromString(context.userId());
    final UUID collectionId = UUID.fromString(context.collectionId());
    int count = Base.exec(AJEntityCollection.COPY_COLLECTION_QUERY, UUID.fromString(copyCollectionId), userId, userId, userId, collectionId , collectionId);
    if (count == 0) {
    	return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    
    Base.exec(AJEntityCollection.COPY_COLLECTION_ITEM_QUERY, userId, userId, UUID.fromString(copyCollectionId) , collectionId);
    return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyCollectionId,
            EventBuilderFactory.getCopyCollectionEventBuilder(copyCollectionId)), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }
}
