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

class CopyAssessmentHandler implements DBHandler {
  private final ProcessorContext context;
  private AJEntityCollection assessment;

  private static final Logger LOGGER = LoggerFactory.getLogger(CopyAssessmentHandler.class);

  public CopyAssessmentHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    // There should be an question id present
    if (context.assessmentId() == null || context.assessmentId().isEmpty()) {
      LOGGER.warn("Missing assessment id");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    // The user should not be anonymous
    if (context.userId() == null || context.userId().isEmpty() || context.userId().equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)) {
      LOGGER.warn("Anonymous user attempting to copy assessment");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    // Fetch the content where type is assessment and it is not deleted already
    // and id is specified id

    LazyList<AJEntityCollection> assessments =
            AJEntityCollection.where(AJEntityCollection.AUTHORIZER_QUERY, AJEntityCollection.ASSESSEMENT, this.context.assessmentId(), false);
    // Question should be present in DB
    if (assessments.size() < 1) {
      LOGGER.warn("Assessment id: {} not present in DB", context.assessmentId());
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    this.assessment = assessments.get(0);
    return AuthorizerBuilder.buildCopyAssessmentAuthorizer(this.context).authorize(assessment);

  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final String copyAssessmentId = UUID.randomUUID().toString();
    final UUID userId = UUID.fromString(context.userId());
    final UUID assessmentId = UUID.fromString(context.assessmentId());
    int count = Base.exec(AJEntityCollection.COPY_ASSESSMENT_QUERY, UUID.fromString(copyAssessmentId), userId, userId, userId, assessmentId);
    if (count == 0) {
      // write validation error
    }
    return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyAssessmentId,
            EventBuilderFactory.getCopyAssessmentEventBuilder(copyAssessmentId)), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }
}
