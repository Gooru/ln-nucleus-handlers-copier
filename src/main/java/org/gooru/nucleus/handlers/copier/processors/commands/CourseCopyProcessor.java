package org.gooru.nucleus.handlers.copier.processors.commands;

import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponse;

/**
 * @author ashish on 2/1/17.
 */
class CourseCopyProcessor extends AbstractCommandProcessor {
    public CourseCopyProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {

    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildCourseRepo(context).copyCourse();
    }
}
