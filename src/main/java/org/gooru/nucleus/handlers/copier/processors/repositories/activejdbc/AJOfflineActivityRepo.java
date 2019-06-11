package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.OfflineActivityRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;


public class AJOfflineActivityRepo implements OfflineActivityRepo {

    private final ProcessorContext context;
    
    public AJOfflineActivityRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse copyOfflineActivity() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyOfflineActivityHandler(context));
    }


}
