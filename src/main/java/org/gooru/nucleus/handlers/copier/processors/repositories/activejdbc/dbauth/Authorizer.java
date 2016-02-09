package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.copier.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;
import org.javalite.activejdbc.Model;


public interface Authorizer<T extends Model> {

  ExecutionResult<MessageResponse> authorize(T model);


}
