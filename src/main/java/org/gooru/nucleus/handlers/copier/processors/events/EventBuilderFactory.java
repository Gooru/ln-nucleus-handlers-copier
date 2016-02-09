package org.gooru.nucleus.handlers.copier.processors.events;

import io.vertx.core.json.JsonObject;

public final class EventBuilderFactory {

  private static final String EVT_RESOURCE_COPY = "event.resource.copy";
  private static final String EVT_QUESTION_COPY = "event.question.copy";
  private static final String EVT_COLLECTION_COPY = "event.collection.copy";
  private static final String EVT_ASSESSMENT_COPY = "event.assessment.copy";
  private static final String EVENT_NAME = "event.name";
  private static final String EVENT_BODY = "event.body";
  private static final String RESOURCE_ID = "resourceId";
  private static final String QUESTION_ID = "questionId";
  private static final String COLLECTION_ID = "collectionId";
  private static final String ASSESSMENT_ID = "assessmentId";

  private EventBuilderFactory() {
    throw new AssertionError();
  }

  public static EventBuilder getCopyResourceEventBuilder(String resourceId) {
    return () -> new JsonObject().put(EVENT_NAME, EVT_RESOURCE_COPY).put(EVENT_BODY, new JsonObject().put(RESOURCE_ID, resourceId));
  }

  public static EventBuilder getCopyQuestionEventBuilder(String questionId) {
    return () -> new JsonObject().put(EVENT_NAME, EVT_QUESTION_COPY).put(EVENT_BODY, new JsonObject().put(QUESTION_ID, questionId));
  }

  public static EventBuilder getCopyCollectionEventBuilder(String collectionId) {
    return () -> new JsonObject().put(EVENT_NAME, EVT_COLLECTION_COPY).put(EVENT_BODY, new JsonObject().put(COLLECTION_ID, collectionId));
  }

  public static EventBuilder getCopyAssessmentEventBuilder(String assessmentId) {
    return () -> new JsonObject().put(EVENT_NAME, EVT_ASSESSMENT_COPY).put(EVENT_BODY, new JsonObject().put(ASSESSMENT_ID, assessmentId));
  }

}
