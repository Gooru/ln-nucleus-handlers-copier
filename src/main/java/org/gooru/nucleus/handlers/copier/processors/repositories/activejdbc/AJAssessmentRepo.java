package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.AssessmentRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public class AJAssessmentRepo implements AssessmentRepo {
    private final ProcessorContext context;

    public AJAssessmentRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse copyAssessment() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyAssessmentHandler(context));
    }

}
