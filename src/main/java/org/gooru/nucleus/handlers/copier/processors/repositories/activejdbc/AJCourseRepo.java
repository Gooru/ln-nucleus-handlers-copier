package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

public class AJCourseRepo implements CourseRepo {
  private final ProcessorContext context;

  public AJCourseRepo(ProcessorContext context) {
    this.context = context;
  }

  @Override
  public MessageResponse copyCourse() {
    return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCopyCourseHandler(context));

  }
}
