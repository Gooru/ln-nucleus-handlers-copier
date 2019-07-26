package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("course")
public class AJEntityCourse extends Model {
    public static final String AUTHORIZER_QUERY = "id = ?::uuid and is_deleted = ?";
    public static final String COPY_COURSE = "SELECT copy_course(?::uuid, ?::uuid, ?::uuid, ?::uuid)";
    private static final String TENANT = "tenant";
    private static final String TENANT_ROOT = "tenant_root";
    private static final String PUBLISH_STATUS = "publish_status";
    private static final String PUBLISH_STATUS_TYPE_PUBLISHED = "published";


    public boolean isCoursePublished() {
        String publishStatus = this.getString(PUBLISH_STATUS);
        return PUBLISH_STATUS_TYPE_PUBLISHED.equalsIgnoreCase(publishStatus);
    }

    public String getTenant() {
        return this.getString(TENANT);
    }

    public String getTenantRoot() {
        return this.getString(TENANT_ROOT);
    }

}
