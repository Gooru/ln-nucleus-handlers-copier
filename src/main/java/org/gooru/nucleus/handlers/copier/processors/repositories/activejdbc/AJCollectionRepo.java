package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public class AJCollectionRepo implements CollectionRepo {
  private final ProcessorContext context;

  public AJCollectionRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public MessageResponse copyCollection() {
    return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyCollectionHandler(context));

  }
}
