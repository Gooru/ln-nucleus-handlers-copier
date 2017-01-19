package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCollection;
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
class TenantCollectionAuthorizer implements Authorizer<AJEntityCollection> {
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantCollectionAuthorizer.class);

    public TenantCollectionAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityCollection model) {
        TenantTree userTenantTree = TenantTreeBuilder.build(context.tenant(), context.tenantRoot());
        TenantTree contentTenantTree = TenantTreeBuilder.build(model.getTenant(), model.getTenantRoot());

        // First validation based on published status of this entity only
        ContentTenantAuthorization authorization = ContentTenantAuthorizationBuilder
            .build(contentTenantTree, userTenantTree, ContentTreeAttributes.build(model.isPublished()));

        if (authorization.canRead()) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        }
        return checkAuthBasedOnCourseBeingPublished(model, userTenantTree, contentTenantTree);
    }

    private ExecutionResult<MessageResponse> checkAuthBasedOnCourseBeingPublished(AJEntityCollection model,
        TenantTree userTenantTree, TenantTree contentTenantTree) {
        ContentTenantAuthorization authorization;
        String courseId = model.getCourseId();
        if (courseId != null) {
            try {
                long published =
                    Base.count(AJEntityCollection.TABLE_COURSE, AJEntityCollection.PUBLISHED_FILTER, courseId);
                if (published >= 1) {
                    // It is published, check that if entity was already published in which case nothing to check
                    // further else check with the new information bit
                    if (!model.isPublished()) {
                        authorization = ContentTenantAuthorizationBuilder
                            .build(contentTenantTree, userTenantTree, ContentTreeAttributes.build(true));
                        if (authorization.canRead()) {
                            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
                        }
                    }
                }
            } catch (DBException e) {
                LOGGER.error("Error checking tenant authorization for Collection/Assessment '{}' for course '{}'",
                    context.assessmentId(), courseId, e);
                return new ExecutionResult<>(
                    MessageResponseFactory.createInternalErrorResponse(RESOURCE_BUNDLE.getString("CP020")),
                    ExecutionResult.ExecutionStatus.FAILED);
            }
        }
        return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("CP015")),
            ExecutionResult.ExecutionStatus.FAILED);
    }
}
