package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.AssessmentRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.CollectionRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.CourseRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.LessonRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.QuestionRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.ResourceRepo;
import org.gooru.nucleus.handlers.copier.processors.repositories.UnitRepo;

public class AJRepoBuilder {

  public ResourceRepo buildResourceRepo(ProcessorContext context) {
    return new AJResourceRepo(context);
  }

  public QuestionRepo buildQuestionRepo(ProcessorContext context) {
    return new AJQuestionRepo(context);
  }

  public CollectionRepo buildCollectionRepo(ProcessorContext context) {
    return new AJCollectionRepo(context);
  }

  public AssessmentRepo buildAssessmentRepo(ProcessorContext context) {
    return new AJAssessmentRepo(context);
  }

  public CourseRepo buildCourseRepo(ProcessorContext context) {
    return new AJCourseRepo(context);
  }

  public UnitRepo buildUnitRepo(ProcessorContext context) {
    return new AJUnitRepo(context);
  }

  public LessonRepo buildLessonRepo(ProcessorContext context) {
    return new AJLessonRepo(context);
  }
}
