package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.exceptions.ExecutionResultWrapperException;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerChainElement;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerChainRunner;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityLesson;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyLessonHandler implements DBHandler {
    private final ProcessorContext context;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyLessonHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private AJEntityCourse targetCourse;
    private AJEntityCourse sourceCourse;

    public CopyLessonHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy lesson");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.courseId()) || !FieldValidator.validateId(context.unitId())
            || !FieldValidator.validateId(context.lessonId())) {
            LOGGER.error(
                "Invalid request, either source course id or source unit id or source lesson id not available. "
                    + "Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP007)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.targetCourseId()) || !FieldValidator
            .validateId(context.targetUnitId())) {
            LOGGER.error("Invalid request, either target course id or target unit id  not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP008)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        try {
            checkLessonExists();
            checkTargetUnitExists();
            initializeTargetCourse();
            initializeSourceCourse();
        } catch (ExecutionResultWrapperException ex) {
            return ex.getResult();
        }

        return checkAuthorization();
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final UUID userId = UUID.fromString(context.userId());
        final UUID lessonId = UUID.fromString(context.lessonId());
        final UUID targetCourseId = UUID.fromString(context.targetCourseId());
        final UUID targetUnitId = UUID.fromString(context.targetUnitId());
        LOGGER.info("Start Time:: ", System.currentTimeMillis());
        Object copyLessonId = Base.firstCell(AJEntityLesson.COPY_LESSON, targetCourseId, targetUnitId, lessonId, userId);
        if (copyLessonId != null) {
          LOGGER.info("End Time:: ", System.currentTimeMillis());
            return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyLessonId.toString(),
                EventBuilderFactory.getCopyLessonEventBuilder(copyLessonId.toString())),
                ExecutionResult.ExecutionStatus.SUCCESSFUL);
        }
        return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
            ExecutionResult.ExecutionStatus.FAILED);

    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

    private ExecutionResult<MessageResponse> checkAuthorization() {
        List<AuthorizerChainElement> chain = new LinkedList<>();
        chain.add(
            new AuthorizerChainElement<>(this.targetCourse, AuthorizerBuilder.buildCopyLessonAuthorizer(this.context)));
        chain.add(new AuthorizerChainElement<>(this.sourceCourse,
            AuthorizerBuilder.buildTenantCourseAuthorizer(this.context)));
        return AuthorizerChainRunner.runChain(chain);
    }

    private void checkLessonExists() {
        long lessonsCount = AJEntityLesson
            .count(AJEntityLesson.LESSON_EXISTS_QUERY, context.courseId(), context.unitId(), context.lessonId(), false);
        // lesson should be present in DB
        if (lessonsCount < 1) {
            LOGGER.warn("Lesson id: {} not present in DB", context.lessonId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP017)),
                ExecutionResult.ExecutionStatus.FAILED));
        }
    }

    private void checkTargetUnitExists() {
        long unitsCount =
            AJEntityUnit.count(AJEntityUnit.UNIT_EXISTS_QUERY, context.targetCourseId(), context.targetUnitId(), false);
        if (unitsCount < 1) {
            LOGGER.warn("Target unit id: {} not present in DB", context.targetUnitId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP019)),
                ExecutionResult.ExecutionStatus.FAILED));
        }
    }

    private void initializeSourceCourse() {
        this.sourceCourse = AJEntityCourse.findFirst(AJEntityCourse.AUTHORIZER_QUERY, context.courseId(), false);
        if (this.sourceCourse == null) {
            LOGGER.warn("Source course id: {} not present in DB", context.courseId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP018)),
                ExecutionResult.ExecutionStatus.FAILED));
        }
    }

    private void initializeTargetCourse() {
        this.targetCourse =
            AJEntityCourse.findFirst(AJEntityCourse.AUTHORIZER_QUERY, context.targetCourseId(), false);
        if (this.targetCourse == null) {
            LOGGER.warn("Target course id: {} not present in DB", context.targetCourseId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP018)),
                ExecutionResult.ExecutionStatus.FAILED));
        }
    }

}
