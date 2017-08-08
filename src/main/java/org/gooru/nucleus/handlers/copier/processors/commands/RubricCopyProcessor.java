package org.gooru.nucleus.handlers.copier.processors.commands;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author szgooru
 * Created On: 07-Mar-2017
 */
public class RubricCopyProcessor extends AbstractCommandProcessor {

    protected RubricCopyProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {
        // NOOP
    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildRubricRepo(context).copyRubric();
    }

}
