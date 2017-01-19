package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.libs.tenant.TenantTree;
import org.gooru.nucleus.libs.tenant.TenantTreeBuilder;
import org.gooru.nucleus.libs.tenant.contents.ContentTenantAuthorization;
import org.gooru.nucleus.libs.tenant.contents.ContentTenantAuthorizationBuilder;
import org.gooru.nucleus.libs.tenant.contents.ContentTreeAttributes;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 10/1/17.
 */
class TenantContentAuthorizer implements Authorizer<AJEntityContent> {
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantContentAuthorizer.class);

    public TenantContentAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityContent model) {
        TenantTree userTenantTree = TenantTreeBuilder.build(context.tenant(), context.tenantRoot());
        TenantTree contentTenantTree = TenantTreeBuilder.build(model.getTenant(), model.getTenantRoot());

        ContentTenantAuthorization authorization = ContentTenantAuthorizationBuilder
            .build(contentTenantTree, userTenantTree, ContentTreeAttributes.build(model.isPublished()));

        if (authorization.canRead()) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        }
        return checkAuthBasedOnParentBeingPublished(model, userTenantTree, contentTenantTree);
    }

    private ExecutionResult<MessageResponse> checkAuthBasedOnParentBeingPublished(AJEntityContent model,
        TenantTree userTenantTree, TenantTree contentTenantTree) {
        ExecutionResult<MessageResponse> result =
            checkAuthBasedOnParents(model, userTenantTree, contentTenantTree, AJEntityContent.TABLE_COURSE,
                model.getCourseId());
        if (result.continueProcessing()) {
            return result;
        }
        return checkAuthBasedOnParents(model, userTenantTree, contentTenantTree, AJEntityContent.TABLE_COLLECTION,
            model.getCollectionId());
    }

    private ExecutionResult<MessageResponse> checkAuthBasedOnParents(AJEntityContent model, TenantTree userTenantTree,
        TenantTree contentTenantTree, String table, String id) {
        ContentTenantAuthorization authorization;
        if (id != null) {
            try {
                long published = Base.count(table, AJEntityContent.PUBLISHED_FILTER, id);
                if (published >= 1) {
                    if (!model.isPublished()) {
                        authorization = ContentTenantAuthorizationBuilder
                            .build(contentTenantTree, userTenantTree, ContentTreeAttributes.build(true));
                        if (authorization.canRead()) {
                            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
                        }
                    }
                }
            } catch (DBException e) {
                LOGGER.error("Error checking authorization for fetch for question '{}' in {} with id '{}'",
                    context.questionId(), table, id, e);
                return new ExecutionResult<>(
                    MessageResponseFactory.createInternalErrorResponse(RESOURCE_BUNDLE.getString("CP020")),
                    ExecutionResult.ExecutionStatus.FAILED);
            }
        }
        return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("CP022")),
            ExecutionResult.ExecutionStatus.FAILED);
    }
}
