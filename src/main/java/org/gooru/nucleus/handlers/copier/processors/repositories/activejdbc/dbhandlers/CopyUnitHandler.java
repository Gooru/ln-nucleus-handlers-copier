package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers;

import java.util.*;

import org.gooru.nucleus.handlers.copier.constants.MessageCodeConstants;
import org.gooru.nucleus.handlers.copier.constants.ParameterConstants;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.copier.processors.exceptions.ExecutionResultWrapperException;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerChainElement;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth.AuthorizerChainRunner;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyUnitHandler implements DBHandler {
    private final ProcessorContext context;
    private final Logger LOGGER = LoggerFactory.getLogger(CopyUnitHandler.class);
    private final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private AJEntityCourse targetCourse;
    private AJEntityCourse sourceCourse;

    public CopyUnitHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (!FieldValidator.validateUser(context.userId())) {
            LOGGER.warn("Anonymous user attempting to copy unit");
            return new ExecutionResult<>(MessageResponseFactory.createForbiddenResponse(),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.courseId()) || !FieldValidator.validateId(context.unitId())) {
            LOGGER.error("Invalid request, either source course id / source  unit id  not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP006)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        if (!FieldValidator.validateId(context.targetCourseId())) {
            LOGGER.error("Invalid request,  target course id  not available. Aborting");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString(MessageCodeConstants.CP009)),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {

        try {
            checkUnitExists();
            initializeTargetCourse();
            initializeSourceCourse();
        } catch (ExecutionResultWrapperException ex) {
            return ex.getResult();
        }

        return checkAuthorization();
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final UUID copyUnitId = UUID.randomUUID();
        final UUID userId = UUID.fromString(context.userId());
        final UUID unitId = UUID.fromString(context.unitId());
        final UUID targetCourseId = UUID.fromString(context.targetCourseId());
        int count =
            Base.exec(AJEntityUnit.COPY_UNIT, targetCourseId, copyUnitId, context.tenant(), context.tenantRoot(),
                userId, userId, userId, targetCourseId, unitId);
        if (count > 0) {
            int lessonCount =
                Base.exec(AJEntityUnit.COPY_LESSON, context.tenant(), context.tenantRoot(), userId, userId, userId,
                    copyUnitId, unitId);
            if (lessonCount > 0) {
                int collectionCount =
                    Base.exec(AJEntityUnit.COPY_COLLECTION, context.tenant(), context.tenantRoot(), userId, userId,
                        userId, copyUnitId, unitId);
                if (collectionCount > 0) {
                    Base.exec(AJEntityUnit.COPY_CONTENT, context.tenant(), context.tenantRoot(), userId, userId,
                        copyUnitId, unitId);
                    Base.exec(AJEntityUnit.COPY_RUBRIC, userId, userId, context.tenant(), context.tenantRoot(),
                        copyUnitId, unitId);
                }
            }
            this.targetCourse.set(ParameterConstants.UPDATED_AT, new Date(System.currentTimeMillis()));
            this.targetCourse.save();
            return new ExecutionResult<>(MessageResponseFactory.createCreatedResponse(copyUnitId.toString(),
                EventBuilderFactory.getCopyUnitEventBuilder(copyUnitId.toString())),
                ExecutionResult.ExecutionStatus.SUCCESSFUL);
        }
        return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(),
            ExecutionResult.ExecutionStatus.FAILED);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

    private ExecutionResult<MessageResponse> checkAuthorization() {
        List<AuthorizerChainElement> chain = new LinkedList<>();
        chain.add(
            new AuthorizerChainElement<>(this.targetCourse, AuthorizerBuilder.buildCopyUnitAuthorizer(this.context)));
        chain.add(new AuthorizerChainElement<>(this.sourceCourse,
            AuthorizerBuilder.buildTenantCourseAuthorizer(this.context)));
        return AuthorizerChainRunner.runChain(chain);
    }

    private void initializeSourceCourse() {
        this.sourceCourse = AJEntityCourse.findFirst(AJEntityCourse.AUTHORIZER_QUERY, context.courseId(), false);
        if (this.sourceCourse == null) {
            LOGGER.warn("Source course id: {} not present in DB", context.courseId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP018)),
                ExecutionResult.ExecutionStatus.FAILED));
        }
    }

    private void initializeTargetCourse() {
        this.targetCourse = AJEntityCourse.findFirst(AJEntityCourse.AUTHORIZER_QUERY, context.targetCourseId(), false);
        if (this.targetCourse == null) {
            LOGGER.warn("Target course id: {} not present in DB", context.targetCourseId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP018)),
                ExecutionResult.ExecutionStatus.FAILED));
        }
    }

    private void checkUnitExists() {
        long units = AJEntityUnit.count(AJEntityUnit.UNIT_EXISTS_QUERY, context.courseId(), context.unitId(), false);
        if (units < 1) {
            LOGGER.warn("Unit id: {} not present in DB", context.unitId());
            throw new ExecutionResultWrapperException(new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString(MessageCodeConstants.CP016)),
                ExecutionResult.ExecutionStatus.FAILED));
        }

    }

}
