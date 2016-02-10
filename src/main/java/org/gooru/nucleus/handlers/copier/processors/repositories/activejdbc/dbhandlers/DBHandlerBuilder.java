package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;

public final class DBHandlerBuilder {

  public static DBHandler buildCopyResourceHandler(ProcessorContext context) {
    return new CopyResourceHandler(context);
  }

  public static DBHandler buildCopyQuestionHandler(ProcessorContext context) {
    return new CopyQuestionHandler(context);

  }

  public static DBHandler buildCopyCollectionHandler(ProcessorContext context) {
    return new CopyCollectionHandler(context);
  }

  public static DBHandler buildCopyAssessmentHandler(ProcessorContext context) {
    return new CopyAssessmentHandler(context);

  }

  public static DBHandler buildCopyCourseHandler(ProcessorContext context) {
    return new CopyCourseHandler(context);
  }

  public static DBHandler buildCopyUnitHandler(ProcessorContext context) {
    return new CopyUnitHandler(context);

  }

  public static DBHandler buildCopyLessonHandler(ProcessorContext context) {
    return new CopyLessonHandler(context);

  }

  private DBHandlerBuilder() {
    throw new AssertionError();
  }
}
