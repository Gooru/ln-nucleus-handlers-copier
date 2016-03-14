package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyResourceHandler implements DBHandler {
  private final ProcessorContext context;
  private AJEntityContent resource;

  private static final Logger LOGGER = LoggerFactory.getLogger(CopyResourceHandler.class);

  public CopyResourceHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    // There should be an resource id present
    if (context.resourceId() == null || context.resourceId().isEmpty()) {
      LOGGER.warn("Missing resource id");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    // The user should not be anonymous
    if (context.userId() == null || context.userId().isEmpty() || context.userId().equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)) {
      LOGGER.warn("Anonymous user attempting to copy resource");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {

    // Fetch the content where type is resource and it is not deleted already
    // and id is specified id

    LazyList<AJEntityContent> resources =
            AJEntityContent.where(AJEntityContent.AUTHORIZER_QUERY, AJEntityContent.RESOURCE, this.context.resourceId(), false);
    // Resource should be present in DB
    if (resources.size() < 1) {
      LOGGER.warn("Resource id: {} not present in DB", context.resourceId());
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    this.resource = resources.get(0);
    return AuthorizerBuilder.buildCopyResourceAuthorizer(this.context).authorize(resource);

  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final String resourceId = UUID.randomUUID().toString();
    final UUID userId = UUID.fromString(this.context.userId());
    final UUID parentResourceId = UUID.fromString(this.context.resourceId());
    int count = Base.exec(AJEntityContent.COPY_RESOURCE_QUERY, UUID.fromString(resourceId), userId, userId, parentResourceId, parentResourceId);
    if (count == 0) {
      return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(resourceId,
            EventBuilderFactory.getCopyResourceEventBuilder(resourceId)), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }

}
