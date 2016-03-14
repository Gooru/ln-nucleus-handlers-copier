package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.LessonRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public class AJLessonRepo implements LessonRepo {
  private final ProcessorContext context;

  public AJLessonRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public MessageResponse copyLesson() {
    return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyLessonHandler(context));
  }
}
