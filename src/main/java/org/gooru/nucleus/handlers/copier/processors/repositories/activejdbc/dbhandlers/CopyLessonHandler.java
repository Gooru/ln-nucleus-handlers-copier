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
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityLesson;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyLessonHandler implements DBHandler {
  private final ProcessorContext context;
  private final Logger LOGGER = LoggerFactory.getLogger(CopyLessonHandler.class);
  private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
  private AJEntityCourse targetCourse;

  public CopyLessonHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    if (!FieldValidator.validateUser(context.userId())) {
      LOGGER.warn("Anonymous user attempting to copy lesson");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    if (!FieldValidator.validateId(context.courseId()) || !FieldValidator.validateId(context.unitId())
        || !FieldValidator.validateId(context.lessonId())) {
      LOGGER.error("Invalid request, either source course id or source unit id or source lesson id not available. Aborting");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP007)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    if (!FieldValidator.validateId(context.targetCourseId()) || !FieldValidator.validateId(context.targetUnitId())) {
      LOGGER.error("Invalid request, either target course id or target unit id  not available. Aborting");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP008)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    LazyList<AJEntityLesson> lessons = AJEntityLesson.where(AJEntityLesson.AUTHORIZER_QUERY, context.lessonId(), false);
    // lesson should be present in DB
    if (lessons.size() < 1) {
      LOGGER.warn("Lesson id: {} not present in DB", context.lessonId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP017)),
          ExecutionResult.ExecutionStatus.FAILED);
    }

    // target course should be present in DB
    LazyList<AJEntityCourse> courses = AJEntityCourse.where(AJEntityCourse.AUTHORIZER_QUERY, context.targetCourseId(), false);
    if (courses.size() < 1) {
      LOGGER.warn("Target course id: {} not present in DB", context.targetCourseId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP018)),
          ExecutionResult.ExecutionStatus.FAILED);
    }

    // target unit should be present in DB
    LazyList<AJEntityUnit> units = AJEntityUnit.where(AJEntityUnit.AUTHORIZER_QUERY, context.targetUnitId(), false);
    if (units.size() < 1) {
      LOGGER.warn("Target unit id: {} not present in DB", context.targetUnitId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP019)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    this.targetCourse = courses.get(0);

    return AuthorizerBuilder.buildCopyLessonAuthorizer(this.context).authorize(this.targetCourse);
  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final UUID copyLessonId = UUID.randomUUID();
    final UUID userId = UUID.fromString(context.userId());
    final UUID lessonId = UUID.fromString(context.lessonId());
    int count =
        Base.exec(AJEntityLesson.COPY_LESSON, UUID.fromString(context.targetCourseId()), UUID.fromString(context.targetUnitId()), copyLessonId,
            userId, userId, userId, lessonId);
    if (count > 0) {
      int collectionCount = Base.exec(AJEntityLesson.COPY_COLLECTION, userId, userId, userId, copyLessonId, lessonId);
      if (collectionCount > 0) {
        Base.exec(AJEntityLesson.COPY_CONTENT, userId, userId, copyLessonId, lessonId);
      }
      this.targetCourse.set(ParameterConstants.UPDATED_AT, new Date(System.currentTimeMillis()));
      this.targetCourse.save();
      return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyLessonId.toString(),
          EventBuilderFactory.getCopyUnitEventBuilder(copyLessonId.toString())), ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }
    return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(), ExecutionResult.ExecutionStatus.FAILED);

  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }
}
