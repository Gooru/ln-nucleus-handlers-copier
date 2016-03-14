package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("content")
public class AJEntityContent extends Model {

  public static final String RESOURCE = "resource";

  public static final String QUESTION = "question";

  public static final String AUTHORIZER_QUERY = "content_format = ?::content_format_type and id = ?::uuid and is_deleted = ?";

  public static final String COPY_RESOURCE_QUERY =
          "insert into content(id, title, url, creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, publish_date, short_title, narration, description, content_format, content_subformat, answer,  metadata,taxonomy, depth_of_knowledge, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, info, visible_on_profile, display_guide, accessibility) select ?, title, url, ?, ?, original_creator_id, original_content_id, ?, publish_date, short_title, narration, description, content_format, content_subformat, answer,  metadata,taxonomy, depth_of_knowledge, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, info, visible_on_profile, display_guide, accessibility from content where id = ? and content_format='resource'";

  public static final String COPY_QUESTION_QUERY =
          "insert into content(id, title, url, creator_id, modifier_id, original_creator_id, original_content_id, publish_date, short_title, narration, description, content_format, content_subformat, answer,  metadata,taxonomy, depth_of_knowledge, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, info, visible_on_profile, display_guide, accessibility) select ?, title, url, ?, ?, original_creator_id, original_content_id, publish_date, short_title, narration, description, content_format, content_subformat, answer,  metadata,taxonomy, depth_of_knowledge, hint_explanation_detail, thumbnail, is_copyright_owner, copyright_owner, info, visible_on_profile, display_guide, accessibility from content where id = ? and content_format='question'";

}