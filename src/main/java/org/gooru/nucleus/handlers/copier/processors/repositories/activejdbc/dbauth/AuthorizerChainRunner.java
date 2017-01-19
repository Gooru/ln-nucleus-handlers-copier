package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import java.util.List;

import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author ashish on 18/1/17.
 */
public final class AuthorizerChainRunner {
    private AuthorizerChainRunner() {
        throw new AssertionError();
    }

    public static ExecutionResult<MessageResponse> runChain(List<AuthorizerChainElement> chainElements) {
        ExecutionResult<MessageResponse> result = new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);;
        for (AuthorizerChainElement chainElement : chainElements) {
            result = chainElement.getAuthorizer().authorize(chainElement.getModel());
            if (result.hasFailed()) {
                break;
            }
        }
        return result;
    }
}
