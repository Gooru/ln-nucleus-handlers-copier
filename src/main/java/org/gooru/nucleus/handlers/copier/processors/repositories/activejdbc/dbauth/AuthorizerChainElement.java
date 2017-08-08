package org.gooru.nucleus.handlers.copier.processors.repositories.activejdbc.dbauth;

import org.javalite.activejdbc.Model;

/**
 * @author ashish on 18/1/17.
 */
public final class AuthorizerChainElement<T extends Model> {
    private final T model;
    private final Authorizer<T> authorizer;

    public AuthorizerChainElement(T model, Authorizer authorizer) {
        this.model = model;
        this.authorizer = authorizer;
    }

    public T getModel() {
        return model;
    }

    public Authorizer<T> getAuthorizer() {
        return authorizer;
    }
}
