package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;
import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityRubric;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On: 07-Mar-2017
 */
public class CopyRubricHandler implements DBHandler {

    private final ProcessorContext context;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyRubricHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private AJEntityRubric rubric;

    public CopyRubricHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy question.");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        if (!FieldValidator.validateId(context.rubricId())) {
            LOGGER.error("Invalid request, source rubric id not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP024)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        this.rubric = AJEntityRubric.findFirst(AJEntityRubric.AUTHORIZER_QUERY, this.context.rubricId());
        if (this.rubric == null) {
            LOGGER.warn("Rubric id: {} not present in DB", context.rubricId());
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP023)),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        return AuthorizerBuilder.buildCopyRubricAuthorizer(context).authorize(rubric);
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final UUID newRubricId = UUID.randomUUID();

        int count = Base.exec(AJEntityRubric.COPY_RUBRIC, newRubricId, context.userId(), context.userId(),
            context.rubricId(), context.rubricId(), context.tenant(), context.tenantRoot(), context.rubricId());
        if (count == 0) {
            LOGGER.error("error while copying rubric");
            return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        LOGGER.info("rubric is copied successfully");
        return new ExecutionResult<>(
            MessageResponseFactory.createCreatedResponse(newRubricId.toString(),
                EventBuilderFactory.getCopyRubricEventBuilder(newRubricId.toString())),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

}
