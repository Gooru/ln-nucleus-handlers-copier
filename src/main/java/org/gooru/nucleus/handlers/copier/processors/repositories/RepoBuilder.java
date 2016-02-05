package org.gooru.nucleus.handlers.copier.processors.repositories;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.AJRepoBuilder;

public class RepoBuilder {

  public ResourceRepo buildResourceRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildResourceRepo(context);
  }

  public QuestionRepo buildQuestionRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildQuestionRepo(context);
  }

  public AssessmentRepo buildAssessmentRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildAssessmentRepo(context);
  }

  public CollectionRepo buildCollectionRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildCollectionRepo(context);
  }

  public CourseRepo buildCourseRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildCourseRepo(context);
  }

  public UnitRepo buildUnitRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildUnitRepo(context);
  }

  public LessonRepo buildLessonRepo(ProcessorContext context) {
    return new AJRepoBuilder().buildLessonRepo(context);
  }

}
