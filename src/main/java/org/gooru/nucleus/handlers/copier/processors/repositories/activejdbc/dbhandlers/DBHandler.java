package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public interface DBHandler {
        ExecutionResult<MessageResponse> checkSanity();

    ExecutionResult<MessageResponse> validateRequest();

    ExecutionResult<MessageResponse> executeRequest();

    boolean handlerReadOnly();
}
