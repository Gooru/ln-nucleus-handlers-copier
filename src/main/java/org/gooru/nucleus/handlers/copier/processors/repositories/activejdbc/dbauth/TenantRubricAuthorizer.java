package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityRubric;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author szgooru
 * Created On: 07-Mar-2017
 */
public class TenantRubricAuthorizer implements Authorizer<AJEntityRubric> {

    private final ProcessorContext context;
    
    public TenantRubricAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityRubric model) {
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

}
