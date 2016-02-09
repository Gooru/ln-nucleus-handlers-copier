package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("collection")
public class AJEntityCollection extends Model {
  public static final String COLLECTION = "collection";

  public static final String ASSESSEMENT = "assessment";

  public static final String AUTHORIZER_QUERY = "format = ?::content_container_type and id = ?::uuid and is_deleted = ?";

  public static final String COPY_COLLECTION_QUERY =
          "insert into collection(id, title, owner_id, creator_id, modifier_id, original_creator_id, original_collection_id, format, thumbnail, learning_objective, audience,  metadata, taxonomy, orientation, url, login_required, setting, grading) select ?, title, ?, ?, ?, original_creator_id, original_collection_id, format, thumbnail, learning_objective, audience, metadata, taxonomy, orientation, url, login_required, setting, grading from collection where id = ? and format='collection'";

  public static final String COPY_ASSESSMENT_QUERY =
          "insert into collection(id, title, owner_id, creator_id, modifier_id, original_creator_id, original_collection_id, format, thumbnail, learning_objective, audience, metadata, taxonomy, orientation, url, login_required, setting, grading) select ?, title, ?, ?, ?, original_creator_id, original_collection_id, format, thumbnail, learning_objective, audience, metadata, taxonomy, orientation, url, login_required, setting, grading from collection where id = ? and format='assessment'";

}
