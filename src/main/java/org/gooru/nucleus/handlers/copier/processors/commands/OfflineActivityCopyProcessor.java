package org.gooru.nucleus.handlers.copier.processors.commands;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;


public class OfflineActivityCopyProcessor extends AbstractCommandProcessor {

    protected OfflineActivityCopyProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {
        // NOOP
    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildOfflineActivityRepo(context).copyOfflineActivity();
    }

}
