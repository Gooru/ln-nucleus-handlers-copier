package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.RubricRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author szgooru
 * Created On: 07-Mar-2017
 */
public class AJRubricRepo implements RubricRepo {

    private final ProcessorContext context;
    
    public AJRubricRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse copyRubric() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyRubricHandler(context));
    }

}
