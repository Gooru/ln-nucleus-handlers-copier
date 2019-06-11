package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 07-Mar-2017
 */
@Table("rubric")
public class AJEntityRubric extends Model {
    
    private static final String TENANT = "tenant";
    private static final String TENANT_ROOT = "tenant_root";

    private static final String PUBLISH_STATUS = "publish_status";
    private static final String PUBLISH_STATUS_PUBLISHED = "published";
    
    public static final String AUTHORIZER_QUERY = "id = ?::uuid and is_deleted = false";
    
    public static final String COPY_RUBRIC = "SELECT copy_rubric(?::uuid, ?::uuid)";
    
    public String getTenant() {
        return this.getString(TENANT);
    }

    public String getTenantRoot() {
        return this.getString(TENANT_ROOT);
    }

    public boolean isPublished() {
        String publishStatus = this.getString(PUBLISH_STATUS);
        return PUBLISH_STATUS_PUBLISHED.equalsIgnoreCase(publishStatus);
    }
}
