package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.constants.ParameterConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyUnitHandler implements DBHandler {
  private final ProcessorContext context;
  private final Logger LOGGER = LoggerFactory.getLogger(CopyUnitHandler.class);
  private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
  private AJEntityCourse targetCourse;

  public CopyUnitHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    if (!FieldValidator.validateUser(context.userId())) {
      LOGGER.warn("Anonymous user attempting to copy unit");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    if (!FieldValidator.validateId(context.courseId()) || !FieldValidator.validateId(context.unitId())) {
      LOGGER.error("Invalid request, either source course id / source  unit id  not available. Aborting");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP006)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    if (!FieldValidator.validateId(context.targetCourseId())) {
      LOGGER.error("Invalid request,  target course id  not available. Aborting");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP009)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    LazyList<AJEntityUnit> units = AJEntityUnit.where(AJEntityUnit.AUTHORIZER_QUERY, context.unitId(), false);
    // unit should be present in DB
    if (units.size() < 1) {
      LOGGER.warn("Unit id: {} not present in DB", context.unitId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP016)),
          ExecutionResult.ExecutionStatus.FAILED);
    }

    // target course should be present in DB
    LazyList<AJEntityCourse> courses = AJEntityCourse.where(AJEntityCourse.AUTHORIZER_QUERY, context.targetCourseId(), false);
    if (courses.size() < 1) {
      LOGGER.warn("Target course id: {} not present in DB", context.targetCourseId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP018)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    this.targetCourse = courses.get(0);
    return AuthorizerBuilder.buildCopyUnitAuthorizer(this.context).authorize(targetCourse);
  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final UUID copyUnitId = UUID.randomUUID();
    final UUID userId = UUID.fromString(context.userId());
    final UUID unitId = UUID.fromString(context.unitId());
    int count = Base.exec(AJEntityUnit.COPY_UNIT, UUID.fromString(context.targetCourseId()), copyUnitId, userId, userId, userId, unitId);
    if (count > 0) {
      int lessonCount = Base.exec(AJEntityUnit.COPY_LESSON, userId, userId, userId, copyUnitId, unitId);
      if (lessonCount > 0) {
        int collectionCount = Base.exec(AJEntityUnit.COPY_COLLECTION, userId, userId, userId, copyUnitId, unitId);
        if (collectionCount > 0) {
          Base.exec(AJEntityUnit.COPY_CONTENT, userId, userId, copyUnitId, unitId);
        }
      }
      this.targetCourse.set(ParameterConstants.UPDATED_AT, new Date(System.currentTimeMillis()));
      this.targetCourse.save();
      return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyUnitId.toString(),
          EventBuilderFactory.getCopyUnitEventBuilder(copyUnitId.toString())), ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }
    return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(), ExecutionResult.ExecutionStatus.FAILED);
  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }
}
