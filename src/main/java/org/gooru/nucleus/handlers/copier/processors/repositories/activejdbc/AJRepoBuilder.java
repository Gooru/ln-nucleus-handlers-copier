package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.AssessmentRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.LessonRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.OfflineActivityRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.QuestionRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.ResourceRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.RubricRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.UnitRepo;

public final class AJRepoBuilder {

    public static ResourceRepo buildResourceRepo(ProcessorContext context) {
        return new AJResourceRepo(context);
    }

    public static QuestionRepo buildQuestionRepo(ProcessorContext context) {
        return new AJQuestionRepo(context);
    }

    public static CollectionRepo buildCollectionRepo(ProcessorContext context) {
        return new AJCollectionRepo(context);
    }

    public static AssessmentRepo buildAssessmentRepo(ProcessorContext context) {
        return new AJAssessmentRepo(context);
    }

    public static CourseRepo buildCourseRepo(ProcessorContext context) {
        return new AJCourseRepo(context);
    }

    public static UnitRepo buildUnitRepo(ProcessorContext context) {
        return new AJUnitRepo(context);
    }

    public static LessonRepo buildLessonRepo(ProcessorContext context) {
        return new AJLessonRepo(context);
    }
    
    public static RubricRepo buildRubricRepo(ProcessorContext context) {
        return new AJRubricRepo(context);
    }
    
    public static OfflineActivityRepo buildOfflineActivityRepo(ProcessorContext context) {
      return new AJOfflineActivityRepo(context);
    }

    private AJRepoBuilder() {
        throw new AssertionError();
    }

}
