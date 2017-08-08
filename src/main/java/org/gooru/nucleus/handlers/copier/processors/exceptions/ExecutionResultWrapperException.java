package org.gooru.nucleus.handlers.copier.processors.exceptions;

import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author ashish on 18/1/17.
 */
public final class ExecutionResultWrapperException extends RuntimeException {

    private final ExecutionResult<MessageResponse> result;

    public ExecutionResultWrapperException(ExecutionResult<MessageResponse> result) {
        this.result = result;
    }

    public ExecutionResult<MessageResponse> getResult() {
        return result;
    }
}
