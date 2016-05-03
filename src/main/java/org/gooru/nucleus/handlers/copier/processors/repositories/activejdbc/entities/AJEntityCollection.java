package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

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
        "insert into collection(id, title, owner_id, creator_id, modifier_id, original_creator_id, original_collection_id, parent_collection_id , format, thumbnail, learning_objective, metadata, taxonomy, orientation, url, login_required, setting, grading) select ?, title, ?, ?, ?, original_creator_id, coalesce(original_collection_id) as original_collection_id , ?, format, thumbnail, learning_objective, metadata, taxonomy, orientation, url, login_required, setting, grading from collection where id = ? and format='collection'";

    public static final String COPY_ASSESSMENT_QUERY =
        "insert into collection(id, title, owner_id, creator_id, modifier_id, original_creator_id, original_collection_id, parent_collection_id, format, thumbnail, learning_objective, metadata, taxonomy, orientation, url, login_required, setting, grading) select ?, title, ?, ?, ?, original_creator_id, coalesce(original_collection_id) as original_collection_id , ? , format, thumbnail, learning_objective, metadata, taxonomy, orientation, url, login_required, setting, grading from collection where id = ? and format='assessment'";

    public static final String COPY_COLLECTION_ITEM_QUERY =
        "insert into content(id, title, url, creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, publish_date, narration, description, content_format, content_subformat, answer,  metadata,taxonomy, hint_explanation_detail, thumbnail, collection_id, is_copyright_owner, copyright_owner, info, visible_on_profile, display_guide, accessibility) select gen_random_uuid() as id , title, url, ?, ?, coalesce(original_creator_id, creator_id) as original_creator_id, coalesce(original_content_id,id) as original_content_id , CASE WHEN content_format = 'resource' THEN coalesce(parent_content_id,id) ELSE id END as parent_content_id, publish_date, narration, description, content_format, content_subformat, answer,  metadata,taxonomy, hint_explanation_detail, thumbnail, ?, is_copyright_owner , copyright_owner, info, visible_on_profile, display_guide, accessibility from content where collection_id = ?";
}
