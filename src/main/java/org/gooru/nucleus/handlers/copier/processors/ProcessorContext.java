package org.gooru.nucleus.handlers.copier.processors;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;
import org.gooru.nucleus.handlers.copier.constants.ParameterConstants;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

public class ProcessorContext {

    final private String userId;
    final private JsonObject prefs;
    final private JsonObject request;
    final private MultiMap requestHeaders;

    public ProcessorContext(String userId, JsonObject prefs, JsonObject request, MultiMap headers) {
        if (prefs == null || userId == null || prefs.isEmpty() || headers == null || headers.isEmpty()) {
            throw new IllegalStateException("Processor Context creation failed because of invalid values");
        }
        this.userId = userId;
        this.prefs = prefs.copy();
        this.request = request != null ? request.copy() : null;
        this.requestHeaders = headers;
    }

    public String userId() {
        return this.userId;
    }

    public JsonObject prefs() {
        return this.prefs.copy();
    }

    public JsonObject request() {
        return this.request;
    }

    public String resourceId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.RESOURCE_ID) : null;
    }

    public String questionId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.QUESTION_ID) : null;
    }

    public String collectionId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.COLLECTION_ID) : null;
    }

    public String assessmentId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.ASSESSMENT_ID) : null;
    }

    public String courseId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.COURSE_ID) : null;
    }

    public String unitId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.UNIT_ID) : null;
    }

    public String lessonId() {
        return this.requestHeaders != null ? this.requestHeaders.get(MessageConstants.LESSON_ID) : null;
    }

    public String targetCourseId() {
        return this.request != null ? this.request.getString(ParameterConstants.TARGET_COURSE_ID) : null;
    }

    public String targetUnitId() {
        return this.request != null ? this.request.getString(ParameterConstants.TARGET_UNIT_ID) : null;
    }

    public String targetLessonId() {
        return this.request != null ? this.request.getString(ParameterConstants.TARGET_LESSON_ID) : null;
    }

    public String targetCollectionId() {
        return this.request != null ? this.request.getString(ParameterConstants.TARGET_COLLECTION_ID) : null;
    }

    public MultiMap requestHeaders() {
        return requestHeaders;
    }
}
