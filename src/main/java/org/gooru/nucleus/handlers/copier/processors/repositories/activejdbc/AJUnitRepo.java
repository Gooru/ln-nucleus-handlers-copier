package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.UnitRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public class AJUnitRepo implements UnitRepo {
  private final ProcessorContext context;

  public AJUnitRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public MessageResponse copyUnit() {
    return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyUnitHandler(context));

  }
}
