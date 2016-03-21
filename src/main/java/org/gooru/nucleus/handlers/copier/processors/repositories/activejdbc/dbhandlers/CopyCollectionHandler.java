package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.constants.ParameterConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityLesson;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.copier.utils.InternalHelper;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyCollectionHandler implements DBHandler {
  private final ProcessorContext context;
  private AJEntityCourse targetCourse;
  private final Logger LOGGER = LoggerFactory.getLogger(CopyCollectionHandler.class);
  private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

  public CopyCollectionHandler(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public ExecutionResult<MessageResponse> checkSanity() {
    if (!InternalHelper.validateUser(context.userId())) {
      LOGGER.warn("Anonymous user attempting to copy collection");
      return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }
    if (!InternalHelper.validateId(context.collectionId())) {
      LOGGER.error("Invalid request, source collection id not available. Aborting");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP003)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    if (!InternalHelper.validateId(context.targetCourseId()) || !InternalHelper.validateId(context.targetUnitId())
        || !InternalHelper.validateId(context.targetLessonId())) {
      LOGGER.error("Invalid request, either target course id or target unit id or target lesson id  not available. Aborting");
      return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP021)),
          ExecutionResult.ExecutionStatus.FAILED);
    }
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  @Override
  public ExecutionResult<MessageResponse> validateRequest() {
    // Fetch the content where type is collection and it is not deleted already
    // and id is specified id

    LazyList<AJEntityCollection> collections =
        AJEntityCollection.where(AJEntityCollection.AUTHORIZER_QUERY, AJEntityCollection.COLLECTION, this.context.collectionId(), false);
    // Collection should be present in DB
    if (collections.size() < 1) {
      LOGGER.warn("Collection id: {} not present in DB", context.collectionId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP013)),
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

    // target lesson should be present in DB
    LazyList<AJEntityLesson> lessons = AJEntityLesson.where(AJEntityLesson.AUTHORIZER_QUERY, context.targetLessonId(), false);
    if (lessons.size() < 1) {
      LOGGER.warn("Target lesson id: {} not present in DB", context.targetLessonId());
      return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP022)),
          ExecutionResult.ExecutionStatus.FAILED);
    }

    this.targetCourse = courses.get(0);

    return AuthorizerBuilder.buildCopyCollectionAuthorizer(this.context).authorize(targetCourse);

  }

  @Override
  public ExecutionResult<MessageResponse> executeRequest() {
    final String copyCollectionId = UUID.randomUUID().toString();
    final UUID userId = UUID.fromString(context.userId());
    final UUID collectionId = UUID.fromString(context.collectionId());
    final UUID targetCourseId = UUID.fromString(context.targetCourseId());
    final UUID targetUnitId = UUID.fromString(context.targetUnitId());
    final UUID targetLessonId = UUID.fromString(context.targetLessonId());
    int count =
        Base.exec(AJEntityCollection.COPY_COLLECTION_QUERY, UUID.fromString(copyCollectionId), targetCourseId, targetUnitId, targetLessonId, userId,
            userId, userId, collectionId, collectionId);
    if (count == 0) {
      return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(), ExecutionResult.ExecutionStatus.FAILED);
    }

    Base.exec(AJEntityCollection.COPY_COLLECTION_ITEM_QUERY, userId, userId, targetCourseId, targetUnitId, targetLessonId,
        UUID.fromString(copyCollectionId), collectionId);
    this.targetCourse.set(ParameterConstants.UPDATED_AT, new Date(System.currentTimeMillis()));
    this.targetCourse.save();

    return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyCollectionId,
        EventBuilderFactory.getCopyCollectionEventBuilder(copyCollectionId)), ExecutionResult.ExecutionStatus.SUCCESSFUL);
  }

  @Override
  public boolean handlerReadOnly() {
    return false;
  }
}
