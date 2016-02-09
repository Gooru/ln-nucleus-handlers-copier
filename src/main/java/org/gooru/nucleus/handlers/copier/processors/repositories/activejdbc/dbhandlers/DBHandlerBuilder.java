package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;

public class DBHandlerBuilder {

  public DBHandler buildCopyResourceHandler(ProcessorContext context) {
    return new CopyResourceHandler(context);
  }

  public DBHandler buildCopyQuestionHandler(ProcessorContext context) {
    return new CopyQuestionHandler(context);

  }

  public DBHandler buildCopyCollectionHandler(ProcessorContext context) {
    return new CopyCollectionHandler(context);
  }

  public DBHandler buildCopyAssessmentHandler(ProcessorContext context) {
    return new CopyAssessmentHandler(context);

  }

  public DBHandler buildCopyCourseHandler(ProcessorContext context) {
    return new CopyCourseHandler(context);
  }

  public DBHandler buildCopyUnitHandler(ProcessorContext context) {
    return new CopyUnitHandler(context);

  }

  public DBHandler buildCopyLessonHandler(ProcessorContext context) {
    return new CopyLessonHandler(context);

  }
}
