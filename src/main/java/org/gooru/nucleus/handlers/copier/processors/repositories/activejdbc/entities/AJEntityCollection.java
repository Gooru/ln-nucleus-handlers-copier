package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import java.util.Objects;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("collection")
public class AJEntityCollection extends Model {
    public static final String COLLECTION = "collection";

    public static final String ASSESSEMENT = "assessment";

    public static final String AUTHORIZER_QUERY =
        "format = ?::content_container_type and id = ?::uuid and is_deleted = ?";

    public static final String FETCH_COLLECTION = "id = ?::uuid and is_deleted = false";

    public static final String COPY_COLLECTION_QUERY =
        "insert into collection(id, tenant, tenant_root, title, owner_id, creator_id, modifier_id, "
            + "original_creator_id, original_collection_id, parent_collection_id , format, thumbnail, "
            + "learning_objective, metadata, taxonomy, url, login_required, setting, grading, license, gut_codes, primary_language) "
            + "select ?, ?::uuid, ?::uuid, title, ?, ?, ?, coalesce(original_creator_id, creator_id) as "
            + "original_creator_id, coalesce(original_collection_id, id) as original_collection_id , ?, format, "
            + "thumbnail, learning_objective, metadata, taxonomy, url, login_required, setting, grading, license, "
            + "gut_codes, primary_language from collection where id = ? and format='collection' and is_deleted = false";

    public static final String COPY_ASSESSMENT_QUERY =
        "insert into collection(id, tenant, tenant_root, title, owner_id, creator_id, modifier_id, "
            + "original_creator_id, original_collection_id, parent_collection_id, format, thumbnail, "
            + "learning_objective, metadata, taxonomy, url, login_required, setting, grading, license, gut_codes, primary_language) "
            + "select ?, ?::uuid, ?::uuid, title, ?, ?, ?, coalesce(original_creator_id, creator_id) as "
            + "original_creator_id, coalesce(original_collection_id, id) as original_collection_id , ? , format, "
            + "thumbnail, learning_objective, metadata, taxonomy, url, login_required, setting, grading, license, "
            + "gut_codes, primary_language from collection where id = ? and format='assessment' and is_deleted = false";

    public static final String COPY_COLLECTION_ITEM_QUERY =
        "insert into content(id, tenant, tenant_root, title, url, creator_id, modifier_id, original_creator_id, original_content_id, "
            + "parent_content_id, publish_date, narration, description, content_format, content_subformat, answer,  "
            + "metadata,taxonomy, hint_explanation_detail, thumbnail, collection_id, is_copyright_owner, "
            + "copyright_owner, info, visible_on_profile, display_guide, accessibility, sequence_id, primary_language, max_score) select gen_random_uuid() as "
            + "id , ?::uuid, ?::uuid, title, url, ?, ?, coalesce(original_creator_id, creator_id) as original_creator_id, coalesce"
            + "(original_content_id,id) as original_content_id , CASE WHEN content_format = 'resource' THEN coalesce"
            + "(parent_content_id,id) ELSE id END as parent_content_id, publish_date, narration, description, "
            + "content_format, content_subformat, answer,  metadata,taxonomy, hint_explanation_detail, thumbnail, ?, "
            + "is_copyright_owner , copyright_owner, info, visible_on_profile, display_guide, accessibility, sequence_id, primary_language, max_score from "
            + "content where collection_id = ? and is_deleted = false";

    public static final String COPY_RUBRIC =
        "INSERT INTO rubric(id, title, url, is_remote, description, categories, feedback_guidance, overall_feedback_required,"
            + " creator_id, modifier_id, original_creator_id, original_rubric_id, parent_rubric_id, metadata, taxonomy, gut_codes,"
            + " thumbnail, tenant, tenant_root, increment, collection_id, content_id, is_rubric, scoring, max_score, grader, primary_language)"
            + " SELECT gen_random_uuid() as id, r.title, r.url, r.is_remote, r.description, r.categories, r.feedback_guidance,"
            + " r.overall_feedback_required, ?, ?, coalesce(r.original_creator_id, r.creator_id) as original_creator_id,"
            + " coalesce(r.original_rubric_id, r.id) as original_rubric_id, coalesce(r.parent_rubric_id, r.id) as parent_rubric_id,"
            + " r.metadata, r.taxonomy, r.gut_codes, r.thumbnail, ?::uuid, ?::uuid, r.increment, ct.collection_id, ct.id, r.is_rubric,"
            + " r.scoring, r.max_score, r.grader, r.primary_language FROM rubric r inner join content ct on ct.parent_content_id = r.content_id WHERE"
            + " ct.collection_id = ?::uuid AND r.collection_id = ?::uuid AND r.is_deleted = false and ct.is_deleted = false";

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
