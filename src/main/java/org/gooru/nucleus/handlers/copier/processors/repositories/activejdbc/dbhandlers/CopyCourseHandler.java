package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyCourseHandler implements DBHandler {
    private final ProcessorContext context;
    private AJEntityCourse course;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyCourseHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

    public CopyCourseHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy course.");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.courseId())) {
            LOGGER.error("Invalid request, source course id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP006)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // Fetch the course and it is not deleted already and id is specified id
        LazyList<AJEntityCourse> courses =
            AJEntityCourse.where(AJEntityCourse.AUTHORIZER_QUERY, this.context.courseId(), false);

        // Course should be present in DB
        if (courses.size() < 1) {
            LOGGER.warn("Course id: {} not present in DB", context.courseId());
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP015)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        this.course = courses.get(0);
        return AuthorizerBuilder.buildCopyCourseAuthorizer(this.context).authorize(course);

    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final UUID copyCourseId = UUID.randomUUID();
        final UUID userId = UUID.fromString(context.userId());
        final UUID courseId = UUID.fromString(context.courseId());
        int count =
            Base.exec(AJEntityCourse.COPY_COURSE, copyCourseId, context.tenant(), context.tenantRoot(), userId, userId,
                userId, courseId, courseId, "[\"" + userId + "\"]", userId, courseId);
        if (count > 0) {
            int unitCount = Base.exec(AJEntityCourse.COPY_UNIT, copyCourseId, context.tenant(), context.tenantRoot(), userId, userId, userId, courseId);
            if (unitCount > 0) {
                int lessonCount = Base.exec(AJEntityCourse.COPY_LESSON, context.tenant(), context.tenantRoot(), userId, userId, userId, copyCourseId, courseId);
                if (lessonCount > 0) {
                    int collectionCount =
                        Base.exec(AJEntityCourse.COPY_COLLECTION, context.tenant(), context.tenantRoot(), userId, userId, userId, copyCourseId, courseId);
                    if (collectionCount > 0) {
                        Base.exec(AJEntityCourse.COPY_CONTENT, context.tenant(), context.tenantRoot(), userId, userId,
                            copyCourseId, courseId);
                    }
                }
            }
            return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyCourseId.toString(),
                EventBuilderFactory.getCopyCourseEventBuilder(copyCourseId.toString())),
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
