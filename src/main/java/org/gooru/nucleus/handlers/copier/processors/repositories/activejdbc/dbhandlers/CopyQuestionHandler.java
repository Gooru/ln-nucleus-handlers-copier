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

class CopyQuestionHandler implements DBHandler {

  private final ProcessorContext context;
  private AJEntityContent question;

  private static final Logger LOGGER = LoggerFactory.getLogger(CopyQuestionHandler.class);

  public CopyQuestionHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    // There should be an question id present
    if (context.questionId() == null || context.questionId().isEmpty()) {
      LOGGER.warn("Missing question id");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    // The user should not be anonymous
    if (context.userId() == null || context.userId().isEmpty() || context.userId().equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)) {
      LOGGER.warn("Anonymous user attempting to copy question");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    // Fetch the content where type is question and it is not deleted already
    // and id is specified id

    LazyList<AJEntityContent> questions =
            AJEntityContent.where(AJEntityContent.AUTHORIZER_QUERY, AJEntityContent.QUESTION, this.context.questionId(), false);
    // Question should be present in DB
    if (questions.size() < 1) {
      LOGGER.warn("Question id: {} not present in DB", context.questionId());
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    this.question = questions.get(0);
    return AuthorizerBuilder.buildCopyQuestionAuthorizer(this.context).authorize(question);

  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final String copyQuestionId = UUID.randomUUID().toString();
    final UUID userId = UUID.fromString(context.userId());
    final UUID questionId = UUID.fromString(context.questionId());
    int count = Base.exec(AJEntityContent.COPY_QUESTION_QUERY, UUID.fromString(copyQuestionId), userId, userId, questionId);
    if (count == 0) {
      // write validation error
    }
    return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyQuestionId,
            EventBuilderFactory.getCopyResourceEventBuilder(copyQuestionId)), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }

}
