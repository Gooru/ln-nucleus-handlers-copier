package org.gooru.nucleus.handlers.copier.processors.events;

import io.vertx.core.json.JsonObject;

public final class EventBuilderFactory {

    private static final String EVT_RESOURCE_COPY = "event.resource.copy";
    private static final String EVT_QUESTION_COPY = "event.question.copy";
    private static final String EVT_COLLECTION_COPY = "event.collection.copy";
    private static final String EVT_ASSESSMENT_COPY = "event.assessment.copy";
    private static final String EVT_COURSE_COPY = "event.course.copy";
    private static final String EVT_UNIT_COPY = "event.unit.copy";
    private static final String EVT_LESSON_COPY = "event.lesson.copy";
    private static final String EVENT_NAME = "event.name";
    private static final String EVENT_BODY = "event.body";
    private static final String ID = "id";

    private EventBuilderFactory() {
        throw new AssertionError();
    }

    public static EventBuilder getCopyResourceEventBuilder(String resourceId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_RESOURCE_COPY).put(EVENT_BODY,
            new JsonObject().put(ID, resourceId));
    }

    public static EventBuilder getCopyQuestionEventBuilder(String questionId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_QUESTION_COPY).put(EVENT_BODY,
            new JsonObject().put(ID, questionId));
    }

    public static EventBuilder getCopyCollectionEventBuilder(String collectionId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_COLLECTION_COPY).put(EVENT_BODY,
            new JsonObject().put(ID, collectionId));
    }

    public static EventBuilder getCopyAssessmentEventBuilder(String assessmentId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_ASSESSMENT_COPY).put(EVENT_BODY,
            new JsonObject().put(ID, assessmentId));
    }

    public static EventBuilder getCopyCourseEventBuilder(String courseId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_COURSE_COPY).put(EVENT_BODY,
            new JsonObject().put(ID, courseId));
    }

    public static EventBuilder getCopyUnitEventBuilder(String unitId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_UNIT_COPY).put(EVENT_BODY, new JsonObject().put(ID, unitId));
    }

    public static EventBuilder getCopyLessonEventBuilder(String lessonId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_LESSON_COPY).put(EVENT_BODY,
            new JsonObject().put(ID, lessonId));
    }
}
