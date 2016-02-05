package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.ResourceRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public class AJResourceRepo implements ResourceRepo {
  private final ProcessorContext context;

  public AJResourceRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public MessageResponse copyResource() {
    return new TransactionExecutor().executeTransaction(new DBHandlerBuilder().buildCopyResourceHandler(context));

  }
}
