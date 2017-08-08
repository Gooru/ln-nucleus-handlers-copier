package org.gooru.nucleus.handlers.copier.processors.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;
import org.gooru.nucleus.handlers.copier.processors.Processor;
import org.gooru.nucleus.handlers.copier.processors.ProcessorContext;
import org.gooru.nucleus.handlers.copier.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 2/1/17.
 */
public enum CommandProcessorBuilder {

    DEFAULT("default") {
        private final Logger LOGGER = LoggerFactory.getLogger(CommandProcessorBuilder.class);
        private final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

        @Override
        public Processor build(ProcessorContext context) {
            return () -> {
                LOGGER.error("Invalid operation type passed in, not able to handle");
                return MessageResponseFactory
                    .createInvalidRequestResponse(RESOURCE_BUNDLE.getString("CP021"));
            };
        }
    },
    RESOURCE_COPY(MessageConstants.MSG_OP_RESOURCE_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new ResourceCopyProcessor(context);
        }
    },
    QUESTION_COPY(MessageConstants.MSG_OP_QUESTION_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new QuestionCopyProcessor(context);
        }
    },
    COLLECTION_COPY(MessageConstants.MSG_OP_COLLECTION_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new CollectionCopyProcessor(context);
        }
    },
    ASSESSMENT_COPY(MessageConstants.MSG_OP_ASSESSMENT_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new AssessmentCopyProcessor(context);
        }
    },
    COURSE_COPY(MessageConstants.MSG_OP_COURSE_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new CourseCopyProcessor(context);
        }
    },
    UNIT_COPY(MessageConstants.MSG_OP_UNIT_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new UnitCopyProcessor(context);
        }
    },
    LESSON_COPY(MessageConstants.MSG_OP_LESSON_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new LessonCopyProcessor(context);
        }
    },
    RUBRIC_COPY(MessageConstants.MSG_OP_RUBRIC_COPY) {
        @Override
        public Processor build(ProcessorContext context) {
            return new RubricCopyProcessor(context);
        }
    };

    private String name;

    CommandProcessorBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, CommandProcessorBuilder> LOOKUP = new HashMap<>();

    static {
        for (CommandProcessorBuilder builder : values()) {
            LOOKUP.put(builder.getName(), builder);
        }
    }

    public static CommandProcessorBuilder lookupBuilder(String name) {
        CommandProcessorBuilder builder = LOOKUP.get(name);
        if (builder == null) {
            return DEFAULT;
        }
        return builder;
    }

    public abstract Processor build(ProcessorContext context);
}
