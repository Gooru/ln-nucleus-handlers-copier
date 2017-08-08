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
    
    public static final String COPY_RUBRIC =
        "INSERT INTO rubric(id, title, url, is_remote, description, categories, feedback_guidance, overall_feedback_required,"
        + " creator_id, modifier_id, original_creator_id, original_rubric_id, parent_rubric_id, metadata, taxonomy,"
        + " gut_codes, thumbnail, created_at, updated_at, tenant, tenant_root, visible_on_profile, is_deleted, creator_system) SELECT ?, title,"
        + " url, is_remote, description, categories, feedback_guidance, overall_feedback_required, ?::uuid, ?::uuid,"
        + " coalesce(original_creator_id,creator_id) as original_creator_id, coalesce(original_rubric_id,?::uuid) as original_rubric_id, ?::uuid,"
        + " metadata, taxonomy, gut_codes, thumbnail, created_at, updated_at, ?::uuid, ?::uuid, visible_on_profile, is_deleted,"
        + " creator_system FROM rubric WHERE id = ?::uuid AND is_deleted = false";
    
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
