package org.gooru.nucleus.handlers.copier.constants;

public final class MessageConstants {

    public static final String MSG_HEADER_OP = "mb.operation";
    public static final String MSG_HEADER_TOKEN = "session.token";
    public static final String MSG_OP_STATUS = "mb.operation.status";
    public static final String MSG_KEY_SESSION = "session";
    public static final String MSG_OP_STATUS_SUCCESS = "success";
    public static final String MSG_OP_STATUS_ERROR = "error";
    public static final String MSG_OP_STATUS_VALIDATION_ERROR = "error.validation";
    public static final String MSG_USER_ANONYMOUS = "anonymous";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG_HTTP_STATUS = "http.status";
    public static final String MSG_HTTP_BODY = "http.body";
    public static final String MSG_HTTP_RESPONSE = "http.response";
    public static final String MSG_HTTP_ERROR = "http.error";
    public static final String MSG_HTTP_VALIDATION_ERROR = "http.validation.error";
    public static final String MSG_HTTP_HEADERS = "http.headers";
    public static final String MSG_MESSAGE = "message";

    // Operation names: Also need to be updated in corresponding handlers
    public static final String MSG_OP_RESOURCE_COPY = "resource.copy";
    public static final String MSG_OP_QUESTION_COPY = "question.copy";
    public static final String MSG_OP_COLLECTION_COPY = "collection.copy";
    public static final String MSG_OP_ASSESSMENT_COPY = "assessment.copy";
    public static final String MSG_OP_COURSE_COPY = "course.copy";
    public static final String MSG_OP_UNIT_COPY = "unit.copy";
    public static final String MSG_OP_LESSON_COPY = "lesson.copy";

    // Containers for different responses
    public static final String RESP_CONTAINER_MBUS = "mb.container";
    public static final String RESP_CONTAINER_EVENT = "mb.event";

    public static final String RESOURCE_ID = "resourceId";
    public static final String QUESTION_ID = "questionId";
    public static final String COLLECTION_ID = "collectionId";
    public static final String ASSESSMENT_ID = "assessmentId";
    public static final String COURSE_ID = "courseId";
    public static final String UNIT_ID = "unitId";
    public static final String LESSON_ID = "lessonId";

    private MessageConstants() {
        throw new AssertionError();
    }
}
