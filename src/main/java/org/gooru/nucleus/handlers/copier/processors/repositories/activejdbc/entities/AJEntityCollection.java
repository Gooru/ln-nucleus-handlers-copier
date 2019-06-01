package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import java.util.Objects;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("collection")
public class AJEntityCollection extends Model {
    public static final String COLLECTION = "collection";

    public static final String ASSESSEMENT = "assessment";
    
    public static final String OFFLINE_ACTIVITY = "offline-activity";

    public static final String AUTHORIZER_QUERY =
        "format = ?::content_container_type and id = ?::uuid and is_deleted = ?";

    public static final String FETCH_COLLECTION = "id = ?::uuid and is_deleted = false";

    public static final String COPY_COLLECTION_QUERY = "SELECT copy_collection(?::uuid, ? , ?::uuid)";    

    private static final String PUBLISH_STATUS = "publish_status";
    private static final String PUBLISH_STATUS_PUBLISHED = "published";
    private static final String TENANT = "tenant";
    private static final String TENANT_ROOT = "tenant_root";
    private static final String COURSE_ID = "course_id";

    public static final String TABLE_COURSE = "course";
    public static final String PUBLISHED_FILTER = "id = ?::uuid and publish_status = 'published'::publish_status_type;";

    public boolean isPublished() {
        return Objects.equals(this.getString(PUBLISH_STATUS), PUBLISH_STATUS_PUBLISHED);
    }

    public String getCourseId() {
        return this.getString(COURSE_ID);
    }

    public String getTenant() {
        return this.getString(AJEntityCollection.TENANT);
    }

    public String getTenantRoot() {
        return this.getString(AJEntityCollection.TENANT_ROOT);
    }

}
