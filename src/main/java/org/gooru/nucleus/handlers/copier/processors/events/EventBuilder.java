package org.gooru.nucleus.handlers.copier.processors.events;

import io.vertx.core.json.JsonObject;

/**
 * Created by ashish on 19/1/16.
 */
public interface EventBuilder {

  JsonObject build();
}
