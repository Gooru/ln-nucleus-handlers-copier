package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityOriginalResource;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;

public final class AuthorizerBuilder {

    private AuthorizerBuilder() {
        throw new AssertionError();
    }

    public static Authorizer<AJEntityOriginalResource> buildCopyResourceAuthorizer(ProcessorContext context) {
        return new TenantResourceAuthorizer(context);
    }

    public static Authorizer<AJEntityContent> buildCopyResourceRefAuthorizer(ProcessorContext context) {
        return new TenantContentAuthorizer(context);
    }

    public static Authorizer<AJEntityContent> buildCopyQuestionAuthorizer(ProcessorContext context) {
        return new TenantContentAuthorizer(context);
    }

    public static Authorizer<AJEntityCollection> buildCopyCollectionAuthorizer(ProcessorContext context) {
        return new TenantCollectionAuthorizer(context);
    }

    public static Authorizer<AJEntityCollection> buildCopyAssessmentAuthorizer(ProcessorContext context) {
        return new TenantCollectionAuthorizer(context);
    }

    public static Authorizer<AJEntityCourse> buildCopyCourseAuthorizer(ProcessorContext context) {
        return new TenantCourseAuthorizer(context);
    }

    public static Authorizer<AJEntityCourse> buildCopyUnitAuthorizer(ProcessorContext context) {
        return new CopyULCAuthorizer(context);
    }

    public static Authorizer<AJEntityCourse> buildCopyLessonAuthorizer(ProcessorContext context) {
        return new CopyULCAuthorizer(context);
    }

    public static Authorizer<AJEntityCourse> buildTenantCourseAuthorizer(ProcessorContext context) {
        return new TenantCourseAuthorizer(context);
    }
}
