package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("unit")
public class AJEntityUnit extends Model {
    public static final String AUTHORIZER_QUERY = "unit_id = ?::uuid and is_deleted = ?";
    public static final String COPY_UNIT =
        "insert into unit(course_id, unit_id, tenant, tenant_root, title, owner_id, creator_id, modifier_id, "
            + "original_creator_id, original_unit_id, parent_unit_id, big_ideas, essential_questions, metadata, "
            + "taxonomy, sequence_id, aggregated_taxonomy, gut_codes, aggregated_gut_codes, primary_language) select ?, ?, ?::uuid, "
            + "?::uuid, title, ?, ?, ?, coalesce(original_creator_id, creator_id) as original_creator_id, coalesce"
            + "(original_unit_id, unit_id) as original_unit_id, unit_id, big_ideas, essential_questions, metadata, "
            + "taxonomy, (select (coalesce(max(sequence_id), 0) + 1) from unit where course_id = ?) as sequence_id, "
            + "aggregated_taxonomy, gut_codes, aggregated_gut_codes, primary_language from unit where unit_id = ? and "
            + "is_deleted = false";
    public static final String COPY_LESSON =
        "insert into lesson(course_id, unit_id, lesson_id, tenant, tenant_root, title, owner_id, creator_id, "
            + "modifier_id, original_creator_id, original_lesson_id, parent_lesson_id, metadata, taxonomy, "
            + "sequence_id, aggregated_taxonomy, gut_codes, aggregated_gut_codes, primary_language) select u.course_id, u.unit_id, "
            + "gen_random_uuid(), ?::uuid, ?::uuid, l.title, ?, ?, ?, coalesce(l.original_creator_id, l.creator_id) "
            + "as original_creator_id, coalesce(l.original_lesson_id, l.lesson_id) as original_lesson_id, l"
            + ".lesson_id, l.metadata, l.taxonomy, l.sequence_id, l.aggregated_taxonomy, l.gut_codes, l"
            + ".aggregated_gut_codes, l.primary_language from lesson l inner join unit u  on u.parent_unit_id = l.unit_id   where u"
            + ".unit_id = ? and l.unit_id = ? and l.is_deleted = false";
    public static final String COPY_COLLECTION =
        "insert into collection(id, course_id, unit_id, lesson_id, tenant, tenant_root, title, owner_id, creator_id, "
            + "modifier_id, original_creator_id, original_collection_id, parent_collection_id, sequence_id, format, "
            + "thumbnail, learning_objective, metadata, taxonomy, login_required,setting,grading, url, license, "
            + "gut_codes, primary_language) select gen_random_uuid(), l.course_id, l.unit_id, l.lesson_id, ?::uuid, ?::uuid, c.title, "
            + "?, ?, ?, coalesce(c.original_creator_id, c.creator_id) as original_creator_id, coalesce(c"
            + ".original_collection_id, c.id) as original_collection_id, c.id, c.sequence_id, c.format, c.thumbnail, "
            + "c.learning_objective, c.metadata, c.taxonomy, c.login_required,c.setting,c.grading, c.url, c.license, "
            + "c.gut_codes, c.primary_language from collection c inner join lesson l on l.parent_lesson_id = c.lesson_id where l.unit_id "
            + "= ? and c.unit_id = ? and c.is_deleted  = false";
    public static final String COPY_CONTENT =
        "insert into content(id, course_id, unit_id, lesson_id, collection_id, tenant, tenant_root, title, "
            + "creator_id, modifier_id, original_creator_id, original_content_id, parent_content_id, narration, "
            + "description, content_format, content_subformat, answer, metadata, taxonomy, hint_explanation_detail, "
            + "thumbnail, is_copyright_owner, copyright_owner, info, display_guide, accessibility, url, sequence_id, primary_language, max_score) "
            + "select gen_random_uuid(), c.course_id, c.unit_id, c.lesson_id, c.id, ?::uuid, ?::uuid, ct.title, ?, ?,"
            + " coalesce(ct.original_creator_id, ct.creator_id) as original_creator_id, coalesce(ct"
            + ".original_content_id, ct.id) as original_content_id, CASE WHEN ct.content_format = 'resource' THEN "
            + "coalesce(ct.parent_content_id,ct.id) ELSE ct.id END as parent_content_id,ct.narration, ct.description,"
            + " ct.content_format, ct.content_subformat, ct.answer, ct.metadata, ct.taxonomy, ct"
            + ".hint_explanation_detail, ct.thumbnail, ct.is_copyright_owner, ct.copyright_owner, ct.info, ct"
            + ".display_guide, ct.accessibility, ct.url, ct.sequence_id, ct.primary_language, ct.max_score from content ct inner join collection c on c"
            + ".parent_collection_id = ct.collection_id   where c.unit_id = ? and ct.unit_id = ? and c.is_deleted  = "
            + "false and ct.is_deleted  = false";

    public static final String COPY_RUBRIC =
        "INSERT INTO rubric(id, title, url, is_remote, description, categories, feedback_guidance, "
            + "overall_feedback_required,"
            + " creator_id, modifier_id, original_creator_id, original_rubric_id, parent_rubric_id, metadata, "
            + "taxonomy, gut_codes,"
            + " thumbnail, tenant, tenant_root, increment, course_id, unit_id, lesson_id, collection_id, content_id, "
            + "is_rubric,"
            + " scoring, max_score, grader, primary_language) SELECT gen_random_uuid() as id, r.title, r.url, r.is_remote, r"
            + ".description, r.categories,"
            + " r.feedback_guidance, r.overall_feedback_required, ?, ?, coalesce(r.original_creator_id, r.creator_id)"
            + " as"
            + " original_creator_id, coalesce(r.original_rubric_id, r.id) as original_rubric_id, coalesce(r"
            + ".parent_rubric_id, r.id) as"
            + " parent_rubric_id, r.metadata, r.taxonomy, r.gut_codes, r.thumbnail, ?::uuid, ?::uuid, r.increment, ct"
            + ".course_id,"
            + " ct.unit_id, ct.lesson_id, ct.collection_id, ct.id, r.is_rubric, r.scoring, r.max_score, r.grader, r.primary_language FROM"
            + " rubric r inner"
            + " join content ct on ct.parent_content_id = r.content_id WHERE ct.unit_id = ?::uuid AND r.unit_id = "
            + "?::uuid AND r.is_deleted"
            + "  = false and ct.is_deleted = false";

    public static final String UNIT_EXISTS_QUERY = "course_id = ?::uuid and unit_id = ?::uuid and is_deleted = ?";
}
