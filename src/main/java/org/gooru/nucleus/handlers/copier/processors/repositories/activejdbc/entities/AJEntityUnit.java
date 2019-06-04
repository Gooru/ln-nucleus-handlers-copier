package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("unit")
public class AJEntityUnit extends Model {
    public static final String AUTHORIZER_QUERY = "unit_id = ?::uuid and is_deleted = ?";
    public static final String COPY_UNIT = "SELECT copy_unit(?::uuid, ?::uuid, ?::uuid, ?::uuid)";
    public static final String UNIT_EXISTS_QUERY = "course_id = ?::uuid and unit_id = ?::uuid and is_deleted = ?";
    
}
