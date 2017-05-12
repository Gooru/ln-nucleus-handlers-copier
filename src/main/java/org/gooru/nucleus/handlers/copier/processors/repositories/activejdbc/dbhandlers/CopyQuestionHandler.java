package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyQuestionHandler implements DBHandler {
    private final ProcessorContext context;
    private AJEntityContent question;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyQuestionHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

    public CopyQuestionHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy question.");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.questionId())) {
            LOGGER.error("Invalid request, source question id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP001)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // Fetch the content where type is question and it is not deleted already and id is specified id

        this.question = AJEntityContent
            .findFirst(AJEntityContent.AUTHORIZER_QUERY, AJEntityContent.QUESTION, this.context.questionId(), false);
        // Question should be present in DB
        if (this.question == null) {
            LOGGER.warn("Question id: {} not present in DB", context.questionId());
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP012)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return AuthorizerBuilder.buildCopyQuestionAuthorizer(this.context).authorize(question);

    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final String questionId = UUID.randomUUID().toString();
        final UUID userId = UUID.fromString(context.userId());
        final UUID parentQuestionId = UUID.fromString(context.questionId());
        int count = Base.exec(AJEntityContent.COPY_QUESTION_QUERY, UUID.fromString(questionId), context.tenant(),
            context.tenantRoot(), userId, userId, parentQuestionId, parentQuestionId, parentQuestionId);
        if (count == 0) {
            return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        
        //Copy rubric
        int copyRubricCount = Base.exec(AJEntityContent.COPY_RUBRIC_QUERY, questionId, userId, userId, context.tenant(),
            context.tenantRoot(), context.questionId());

        return new ExecutionResult<>(MessageResponseFactory
            .createCreatedResponse(questionId, EventBuilderFactory.getCopyQuestionEventBuilder(questionId)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

}
