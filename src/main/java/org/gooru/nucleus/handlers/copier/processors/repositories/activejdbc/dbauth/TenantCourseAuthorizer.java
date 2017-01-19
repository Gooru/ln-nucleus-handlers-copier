package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.libs.tenant.TenantTree;
import org.gooru.nucleus.libs.tenant.TenantTreeBuilder;
import org.gooru.nucleus.libs.tenant.contents.ContentTenantAuthorization;
import org.gooru.nucleus.libs.tenant.contents.ContentTenantAuthorizationBuilder;
import org.gooru.nucleus.libs.tenant.contents.ContentTreeAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 16/1/17.
 */
class TenantCourseAuthorizer implements Authorizer<AJEntityCourse> {
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantCourseAuthorizer.class);

    public TenantCourseAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityCourse model) {
        TenantTree userTenantTree = TenantTreeBuilder.build(context.tenant(), context.tenantRoot());
        TenantTree contentTenantTree = TenantTreeBuilder.build(model.getTenant(), model.getTenantRoot());

        ContentTenantAuthorization authorization = ContentTenantAuthorizationBuilder
            .build(contentTenantTree, userTenantTree, ContentTreeAttributes.build(model.isCoursePublished()));

        if (authorization.canRead()) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        }
        return new ExecutionResult<>(
            MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("CP015")),
            ExecutionResult.ExecutionStatus.FAILED);
    }
}
