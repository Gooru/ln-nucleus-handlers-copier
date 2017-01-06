package org.gooru.nucleus.handlers.copier.processors;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;
import org.gooru.nucleus.handlers.copier.constants.ParameterConstants;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

public class ProcessorContext {

    private final String userId;
    private final JsonObject session;
    private final JsonObject request;
    private final MultiMap requestHeaders;
    private final TenantContext tenantContext;

    public ProcessorContext(String userId, JsonObject session, JsonObject request, MultiMap headers) {
        if (session == null || userId == null || session.isEmpty() || headers == null || headers.isEmpty()) {
            throw new IllegalStateException("Processor Context creation failed because of invalid values");
        }
        this.userId = userId;
        this.session = session.copy();
        this.request = request != null ? request.copy() : null;
        this.requestHeaders = headers;
        this.tenantContext = new TenantContext(session);
    }

    public String userId() {
        return this.userId;
    }

    public JsonObject session() {
        return this.session.copy();
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

    public String tenant() {
        return this.tenantContext.tenant();
    }

    public String tenantRoot() {
        return this.tenantContext.tenantRoot();
    }

    private static class TenantContext {
        private static final String TENANT = "tenant";
        private static final String TENANT_ID = "tenant_id";
        private static final String TENANT_ROOT = "tenant_root";

        private final String tenantId;
        private final String tenantRoot;

        TenantContext(JsonObject session) {
            JsonObject tenantJson = session.getJsonObject(TENANT);
            if (tenantJson == null || tenantJson.isEmpty()) {
                throw new IllegalStateException("Tenant Context invalid");
            }
            this.tenantId = tenantJson.getString(TENANT_ID);
            if (tenantId == null || tenantId.isEmpty()) {
                throw new IllegalStateException("Tenant Context with invalid tenant");
            }
            this.tenantRoot = tenantJson.getString(TENANT_ROOT);
        }

        public String tenant() {
            return this.tenantId;
        }

        public String tenantRoot() {
            return this.tenantRoot;
        }
    }
}
