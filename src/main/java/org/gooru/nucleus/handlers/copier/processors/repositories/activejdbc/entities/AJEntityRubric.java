package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 07-Mar-2017
 */
@Table("rubric")
public class AJEntityRubric extends Model {
    
    public static final String AUTHORIZER_QUERY = "id = ?::uuid and is_deleted = false";
    
    public static final String COPY_RUBRIC =
        "INSERT INTO rubric(id, title, url, is_remote, description, categories, type, feedback_guidance, total_points, overall_feedback_required,"
        + " creator_id, modifier_id, original_creator_id, original_rubric_id, parent_rubric_id, publish_date, publish_status, metadata, taxonomy,"
        + " gut_codes, thumbnail, created_at, updated_at, tenant, tenant_root, visible_on_profile, is_deleted, creator_system) SELECT ?, title,"
        + " url, is_remote, description, categories, type, feedback_guidance, total_points, overall_feedback_required, ?::uuid, ?::uuid,"
        + " coalesce(original_creator_id,creator_id) as original_creator_id, coalesce(original_rubric_id,?::uuid) as original_rubric_id, ?::uuid, publish_date,"
        + " publish_status, metadata, taxonomy, gut_codes, thumbnail, created_at, updated_at, ?::uuid, ?::uuid, visible_on_profile, is_deleted,"
        + " creator_system FROM rubric WHERE id = ?::uuid AND is_deleted = false";
}
