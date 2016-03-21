package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import io.vertx.core.json.JsonArray;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.constants.ParameterConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class CopyContentAuthorizer implements Authorizer<AJEntityCollection> {

  private final ProcessorContext context;
  private final Logger LOG = LoggerFactory.getLogger(Authorizer.class);
  private final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

  CopyContentAuthorizer(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> authorize(AJEntityCollection collection) {
    String ownerId = collection.getString(ParameterConstants.OWNER_ID);
    //  user should be either owner or collaborator on target collection 
    if (context.userId().equalsIgnoreCase(ownerId)) {
      return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    } else {
        String collaborators = collection.getString(ParameterConstants.COLLABORATOR);
        if (collaborators != null && !collaborators.isEmpty()) {
          JsonArray collaboratorsArray = new JsonArray(collaborators);
          if (collaboratorsArray.contains(context.userId())) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
          }
        }
      }
    LOG.warn("User: '{}' is not owner/collaborator of target collection: '{}' or owner/collaborator on collection", context.userId(), context.courseId());
  
    return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(resourceBundle.getString(MessageCodeConstants.CP010)),
      ExecutionResult.ExecutionStatus.FAILED);
  }
}
