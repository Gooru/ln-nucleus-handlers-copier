package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("content")
public class AJEntityContent extends Model {

    public static final String RESOURCE = "resource";

    public static final String QUESTION = "question";

    public static final String AUTHORIZER_QUERY =
        "content_format = ?::content_format_type and id = ?::uuid and is_deleted = ?";

    public static final String COPY_RESOURCE_QUERY =
        "INSERT INTO content(id, title, url, creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, publish_date,"
        + " publish_status, narration, description, content_format, content_subformat, metadata, taxonomy, thumbnail, is_copyright_owner,"
        + " copyright_owner, info, visible_on_profile, display_guide, accessibility, creator_system) SELECT ?, title, url, ?, ?, creator_id, ?, ?,"
        + " publish_date, publish_status, narration, description, 'resource', content_subformat, metadata, taxonomy, thumbnail, is_copyright_owner,"
        + " copyright_owner, info, visible_on_profile, display_guide, accessibility, creator_system FROM original_resource WHERE id = ?"
        + " AND is_deleted=false";
    
    public static final String COPY_QUESTION_QUERY =
        "insert into content(id, title, url, creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, narration,"
        + " description, content_format, content_subformat, answer,  metadata,taxonomy, hint_explanation_detail, thumbnail, is_copyright_owner,"
        + " copyright_owner, info, visible_on_profile, display_guide, accessibility) select ?, title, url, ?, ?, coalesce(original_creator_id,"
        + "creator_id) as original_creator_id, coalesce(original_content_id,?) as original_content_id, ?, narration, description, content_format,"
        + " content_subformat, answer,  metadata,taxonomy, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, info,"
        + " visible_on_profile, display_guide, accessibility from content where id = ? and content_format='question' and is_deleted=false";

}
