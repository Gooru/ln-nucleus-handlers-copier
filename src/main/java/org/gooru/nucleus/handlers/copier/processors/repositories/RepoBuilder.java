package org.gooru.nucleus.handlers.copier.processors.repositories;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.AJRepoBuilder;

public final class RepoBuilder {

    public static ResourceRepo buildResourceRepo(ProcessorContext context) {
        return AJRepoBuilder.buildResourceRepo(context);
    }

    public static QuestionRepo buildQuestionRepo(ProcessorContext context) {
        return AJRepoBuilder.buildQuestionRepo(context);
    }

    public static AssessmentRepo buildAssessmentRepo(ProcessorContext context) {
        return AJRepoBuilder.buildAssessmentRepo(context);
    }

    public static CollectionRepo buildCollectionRepo(ProcessorContext context) {
        return AJRepoBuilder.buildCollectionRepo(context);
    }

    public static CourseRepo buildCourseRepo(ProcessorContext context) {
        return AJRepoBuilder.buildCourseRepo(context);
    }

    public static UnitRepo buildUnitRepo(ProcessorContext context) {
        return AJRepoBuilder.buildUnitRepo(context);
    }

    public static LessonRepo buildLessonRepo(ProcessorContext context) {
        return AJRepoBuilder.buildLessonRepo(context);
    }

    private RepoBuilder() {
        throw new AssertionError();
    }
}
