package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("original_resource")
public class AJEntityOriginalResource extends Model {

    private static final String TENANT = "tenant";
    private static final String TENANT_ROOT = "tenant_root";
    private static final String PUBLISH_STATUS = "publish_status";
    private static final String PUBLISH_STATUS_PUBLISHED = "published";

    public static final String AUTHORIZER_QUERY = "id = ?::uuid and is_deleted = ?";

    public String getTenant() {
        return this.getString(TENANT);
    }

    public String getTenantRoot() {
        return this.getString(TENANT_ROOT);
    }

    public boolean isResourcePublished() {
        String publishStatus = this.getString(PUBLISH_STATUS);
        return PUBLISH_STATUS_PUBLISHED.equalsIgnoreCase(publishStatus);
    }

}
