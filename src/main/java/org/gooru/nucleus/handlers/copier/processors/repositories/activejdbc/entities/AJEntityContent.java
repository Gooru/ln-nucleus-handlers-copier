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

    public static final String COPY_ORIGINAL_RESOURCE_QUERY =
        "INSERT INTO content(id, tenant, tenant_root, title, url, creator_id, modifier_id, original_creator_id, "
            + "original_content_id, parent_content_id, publish_date, publish_status, narration, description, "
            + "content_format, content_subformat, metadata, taxonomy, thumbnail, is_copyright_owner, copyright_owner,"
            + " info, visible_on_profile, display_guide, accessibility, creator_system) SELECT ?, ?::uuid, ?::uuid, "
            + "title, url, ?, ?, creator_id, ?, ?, publish_date, publish_status, narration, description, 'resource', "
            + "content_subformat, metadata, taxonomy, thumbnail, is_copyright_owner, copyright_owner, info, "
            + "visible_on_profile, display_guide, accessibility, creator_system FROM original_resource WHERE id = ? "
            + "AND is_deleted=false";

    public static final String COPY_REFERENCE_RESOURCE_QUERY =
        "INSERT INTO content(id, tenant, tenant_root, title, url, creator_id, modifier_id, original_creator_id, "
            + "original_content_id, parent_content_id, publish_date, publish_status, narration, description, "
            + "content_format, content_subformat, metadata, taxonomy, thumbnail, is_copyright_owner, copyright_owner,"
            + " info, visible_on_profile, display_guide, accessibility, creator_system) SELECT ?, ?::uuid, ?::uuid, "
            + "?, url, ?, ?, ?, ?, ?, publish_date, publish_status, narration, description, 'resource', "
            + "content_subformat, metadata, taxonomy, thumbnail, is_copyright_owner, copyright_owner, info, "
            + "visible_on_profile, display_guide, accessibility, creator_system FROM content WHERE id = ? AND "
            + "is_deleted=false";

    public static final String COPY_QUESTION_QUERY =
        "insert into content(id, tenant, tenant_root, title, url, creator_id, modifier_id, original_creator_id, "
            + "original_content_id, parent_content_id, narration, description, content_format, content_subformat, "
            + "answer,  metadata,taxonomy, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, "
            + "info, visible_on_profile, display_guide, accessibility) select ?, ?::uuid, ?::uuid, title, url, ?, ?, "
            + "coalesce(original_creator_id,creator_id) as original_creator_id, coalesce(original_content_id,?) as "
            + "original_content_id, ?, narration, description, content_format, content_subformat, answer,  metadata,"
            + "taxonomy, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, info, "
            + "visible_on_profile, display_guide, accessibility from content where id = ? and "
            + "content_format='question' and is_deleted=false";

    public static final String COPY_RUBRIC_QUERY =
        "INSERT INTO rubric(id, content_id, title, url, is_remote, description, categories, feedback_guidance, overall_feedback_required,"
        + " creator_id, modifier_id, original_creator_id, original_rubric_id, parent_rubric_id, metadata, taxonomy, gut_codes,"
        + " thumbnail, tenant, tenant_root, increment, is_rubric, scoring, max_score, grader) select gen_random_uuid() as id,"
        + " ?::uuid, title, url, is_remote, description, categories, feedback_guidance, overall_feedback_required, ?::uuid, ?::uuid, "
        + "coalesce(original_creator_id, creator_id) as original_creator_id, coalesce(original_rubric_id, id) as original_rubric_id,"
        + " coalesce(parent_rubric_id, id) as parent_rubric_id, metadata, taxonomy, gut_codes, thumbnail, ?::uuid, ?::uuid, increment,"
        + " is_rubric, scoring, max_score, grader FROM rubric where content_id = ?::uuid and is_deleted = false";
    
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
