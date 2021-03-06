package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.transactions;

import java.sql.SQLException;

import org.gooru.nucleus.handlers.copier.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbhandlers.DBHandler;
import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TransactionExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionExecutor.class);

    public static MessageResponse executeTransaction(DBHandler handler) {
        // First validations without any DB
        ExecutionResult<MessageResponse> executionResult = handler.checkSanity();
        // Now we need to run with transaction, if we are going to continue
        if (executionResult.continueProcessing()) {
            executionResult = executeWithTransaction(handler);
        }
        return executionResult.result();

    }

    private static ExecutionResult<MessageResponse> executeWithTransaction(DBHandler handler) {
        ExecutionResult<MessageResponse> executionResult;

        try {
            Base.open(DataSourceRegistry.getInstance().getDefaultDataSource());
            // If we need a read only transaction, then it is time to set up now
            if (handler.handlerReadOnly()) {
                Base.connection().setReadOnly(true);
            }
            Base.openTransaction();
            executionResult = handler.validateRequest();
            if (executionResult.continueProcessing()) {
                executionResult = handler.executeRequest();
                Base.commitTransaction();
            }
            return executionResult;
        } catch (Throwable e) {
            Base.rollbackTransaction();
            LOGGER.error("Caught exeption, need to rollback and abort", e);
            // Most probably we do not know what to do with this, so send
            // internal error
            return new ExecutionResult<>(MessageResponseFactory.createInternalErrorResponse(e.getMessage()),
                ExecutionResult.ExecutionStatus.FAILED);
        } finally {
            if (handler.handlerReadOnly()) {
                // restore the settings
                try {
                    Base.connection().setReadOnly(false);
                } catch (SQLException e) {
                    LOGGER.error("Exception while marking connetion to be read/write", e);
                }
            }
            Base.close();
        }
    }

    private TransactionExecutor() {
        throw new AssertionError();
    }
}
