package org.gooru.nucleus.handlers.copier.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;
import org.gooru.nucleus.handlers.copier.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageProcessor implements Processor {

    private final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private final Message<Object> message;
    private String userId;
    private JsonObject prefs;
    private JsonObject request;

    public MessageProcessor(Message<Object> message) {
        this.message = message;
    }

    @Override
    public MessageResponse process() {
        MessageResponse result;
        try {
            // Validate the message itself
            ExecutionResult<MessageResponse> validateResult = validateAndInitialize();
            if (validateResult.isCompleted()) {
                return validateResult.result();
            }

            final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
            switch (msgOp) {
            case MessageConstants.MSG_OP_RESOURCE_COPY:
                result = processResourceCopy();
                break;
            case MessageConstants.MSG_OP_QUESTION_COPY:
                result = processQuestionCopy();
                break;
            case MessageConstants.MSG_OP_COLLECTION_COPY:
                result = processCollectionCopy();
                break;
            case MessageConstants.MSG_OP_ASSESSMENT_COPY:
                result = processAssessmentCopy();
                break;
            case MessageConstants.MSG_OP_COURSE_COPY:
                result = processCourseCopy();
                break;
            case MessageConstants.MSG_OP_UNIT_COPY:
                result = processUnitCopy();
                break;
            case MessageConstants.MSG_OP_LESSON_COPY:
                result = processLessonCopy();
                break;
            default:
                LOGGER.error("Invalid operation type passed in, not able to handle");
                return MessageResponseFactory.createInvalidRequestResponse("Invalid operation");
            }
            return result;
        } catch (Throwable e) {
            LOGGER.error("Unhandled exception in processing", e);
            return MessageResponseFactory.createInternalErrorResponse();
        }
    }

    private MessageResponse processResourceCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildResourceRepo(context).copyResource();
    }

    private MessageResponse processQuestionCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildQuestionRepo(context).copyQuestion();
    }

    private MessageResponse processCollectionCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildCollectionRepo(context).copyCollection();
    }

    private MessageResponse processAssessmentCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildAssessmentRepo(context).copyAssessment();
    }

    private MessageResponse processCourseCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildCourseRepo(context).copyCourse();
    }

    private MessageResponse processUnitCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildUnitRepo(context).copyUnit();
    }

    private MessageResponse processLessonCopy() {
        ProcessorContext context = createContext();
        return RepoBuilder.buildLessonRepo(context).copyLesson();
    }

    private ProcessorContext createContext() {

        return new ProcessorContext(userId, prefs, request, message.headers());
    }

    private ExecutionResult<MessageResponse> validateAndInitialize() {
        if (message == null || !(message.body() instanceof JsonObject)) {
            LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
            return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        userId = ((JsonObject) message.body()).getString(MessageConstants.MSG_USER_ID);
        if (userId == null) {
            LOGGER.error("Invalid user id passed. Not authorized.");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        prefs = ((JsonObject) message.body()).getJsonObject(MessageConstants.MSG_KEY_PREFS);
        request = ((JsonObject) message.body()).getJsonObject(MessageConstants.MSG_HTTP_BODY);

        if (prefs == null || prefs.isEmpty()) {
            LOGGER.error("Invalid preferences obtained, probably not authorized properly");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        if (request == null) {
            LOGGER.error("Invalid JSON payload on Message Bus");
            return new ExecutionResult<>(MessageResponseFactory.createInvalidRequestResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        // All is well, continue processing
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

}
