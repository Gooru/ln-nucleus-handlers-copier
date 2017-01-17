package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityOriginalResource;
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
 * @author ashish on 17/1/17.
 */
class TenantResourceAuthorizer implements Authorizer<AJEntityOriginalResource> {
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantResourceAuthorizer.class);

    public TenantResourceAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityOriginalResource model) {
        TenantTree userTenantTree = TenantTreeBuilder.build(context.tenant(), context.tenantRoot());
        TenantTree contentTenantTree = TenantTreeBuilder.build(model.getTenant(), model.getTenantRoot());

        ContentTenantAuthorization authorization = ContentTenantAuthorizationBuilder
            .build(contentTenantTree, userTenantTree, ContentTreeAttributes.build(model.isResourcePublished()));

        if (authorization.canRead()) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        }
        return new ExecutionResult<>(
            MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("not.found")),
            ExecutionResult.ExecutionStatus.FAILED);
    }

}
