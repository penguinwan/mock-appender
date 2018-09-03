package com.penguinwan;

import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.stubbing.Answer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;


/**
 * This is a TestRule class, used for mocking logging appender, so that logging events can be asserted.
 * <pre>
 * Usage:
 * {@code
 *  @ Rule
 * public com.penguinwan.MockAppender appender = new com.penguinwan.MockAppender(Service.class);
 * ...
 * assert appender.getLoggingEvents().size() == 1;
 * assert appender.getLoggingEvents().get(0).getFormattedMessage() == "This is log message";
 * }
 * </pre>
 */
public class MockAppender implements TestRule {

    private MockAppenderStatement mockAppenderStatement;
    private Logger logger;
    private Class loggingClazz;

    public MockAppender() {
        this.logger = (Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    public MockAppender(Class clazz) {
        this.loggingClazz = clazz;
        this.logger = (Logger) org.slf4j.LoggerFactory.getLogger(loggingClazz);
    }

    public Statement apply(Statement statement, Description description) {
        mockAppenderStatement = new MockAppenderStatement(statement, logger);
        return mockAppenderStatement;
    }

    public List<LoggingEvent> getLoggingEvents() {
        return mockAppenderStatement.getLoggingEvents();
    }

    private static class MockAppenderStatement extends Statement {

        private Logger logger;
        private Statement base;
        private Appender appender;
        private List<LoggingEvent> events = new LinkedList<>();

        public MockAppenderStatement(Statement base, Logger logger) {
            this.base = base;
            this.logger = logger;

            initAppender();
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                addAppender();
                base.evaluate();
            } finally {
                removeAppender();
            }
        }

        public List<LoggingEvent> getLoggingEvents() {
            return events;
        }

        private void initAppender() {
            Answer answer = (invocationOnMock) -> {
                if (invocationOnMock.getMethod().getName().equals("doAppend")) {
                    LoggingEvent event = invocationOnMock.getArgument(0);
                    events.add(event);
                }
                return null;
            };
            appender = mock(Appender.class, answer);
        }

        private void addAppender() {
            logger.addAppender(appender);
        }

        private void removeAppender() {
            logger.detachAppender(appender);
        }
    }
}

