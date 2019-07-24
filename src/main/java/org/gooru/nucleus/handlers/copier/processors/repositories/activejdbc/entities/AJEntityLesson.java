package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("lesson")
public class AJEntityLesson extends Model {
    public static final String AUTHORIZER_QUERY = "lesson_id = ?::uuid and is_deleted = ?";
    public static final String COPY_LESSON = "SELECT copy_lesson(?::uuid, ?::uuid, ?::uuid, ?::uuid, ?::uuid, ?::uuid, ?::uuid, ?::uuid)";
    public static final String LESSON_EXISTS_QUERY = "course_id = ?::uuid and unit_Id = ?::uuid and lesson_id = ?::uuid and is_deleted = ?";
  
}
