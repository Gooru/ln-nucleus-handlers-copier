package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("content")
public class AJEntityContent extends Model {

    public static final String RESOURCE = "resource";

    public static final String QUESTION = "question";

    public static final String ORIGINAL_CONTENT_ID = "original_content_id";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String TITLE = "title";

    public static final String AUTHORIZER_QUERY =
        "content_format = ?::content_format_type and id = ?::uuid and is_deleted = ?";

    public static final String COPY_ORIGINAL_RESOURCE_QUERY = "SELECT copy_original_resource(?::uuid, ?::uuid, ?::uuid, ?::uuid)";

    public static final String COPY_REFERENCE_RESOURCE_QUERY = "SELECT copy_content(?::uuid, 'resource', ?::uuid, ?::uuid, ?::uuid, ?)";
    
    public static final String COPY_REFERENCE_QUESTION_QUERY = "SELECT copy_content(?::uuid, 'question', ?::uuid, ?::uuid, ?::uuid)";

    public static final String PUBLISHED_FILTER = "id = ?::uuid and publish_status = 'published'::publish_status_type;";

    private static final String TENANT = "tenant";
    private static final String TENANT_ROOT = "tenant_root";

    private static final String PUBLISH_STATUS = "publish_status";
    private static final String PUBLISH_STATUS_PUBLISHED = "published";
    private static final String COLLECTION_ID = "collection_id";
    private static final String COURSE_ID = "course_id";

    public static final String TABLE_COURSE = "course";
    public static final String TABLE_COLLECTION = "collection";

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

    public String getCourseId() {
        return this.getString(COURSE_ID);
    }

    public String getCollectionId() {
        return this.getString(COLLECTION_ID);
    }

}
