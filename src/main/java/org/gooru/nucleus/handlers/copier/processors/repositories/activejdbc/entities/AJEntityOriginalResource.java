package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("original_resource")
public class AJEntityOriginalResource extends Model {

    public static final String AUTHORIZER_QUERY = "id = ?::uuid and is_deleted = ?";
}
