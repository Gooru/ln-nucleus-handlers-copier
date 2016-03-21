package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;

public final class AuthorizerBuilder {

  private AuthorizerBuilder() {
    throw new AssertionError();
  }

  public static Authorizer<AJEntityCollection> buildCopyResourceAuthorizer(ProcessorContext context) {
    return new CopyContentAuthorizer(context);
  }

  public static Authorizer<AJEntityCollection> buildCopyQuestionAuthorizer(ProcessorContext context) {
    return new CopyContentAuthorizer(context);
  }

  public static Authorizer<AJEntityCourse> buildCopyCollectionAuthorizer(ProcessorContext context) {
    return new CopyULCAuthorizer(context);
  }

  public static Authorizer<AJEntityCourse> buildCopyAssessmentAuthorizer(ProcessorContext context) {
    return new CopyULCAuthorizer(context);
  }

  public static Authorizer<AJEntityCourse> buildCopyCourseAuthorizer(ProcessorContext context) {
    return model -> new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

  public static Authorizer<AJEntityCourse> buildCopyUnitAuthorizer(ProcessorContext context) {
    return new CopyULCAuthorizer(context);
  }

  public static Authorizer<AJEntityCourse> buildCopyLessonAuthorizer(ProcessorContext context) {
    return new CopyULCAuthorizer(context);
  }

}
