package org.gooru.nucleus.handlers.copier.processors.commands;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author ashish on 2/1/17.
 */
class CollectionCopyProcessor extends AbstractCommandProcessor {
    public CollectionCopyProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {

    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildCollectionRepo(context).copyCollection();
    }
}
