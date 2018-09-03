package com.penguinwan;

import org.junit.Rule;
import org.junit.Test;

import ch.qos.logback.classic.Level;


public class ServiceTest {

    @Rule
    public MockAppender appender = new MockAppender(Service.class);

    @Test
    public void writeLogWhenExecute() {
        Service service = new Service();
        service.writeLogMessage();

        assert appender.getLoggingEvents().size() == 3;
        assert appender.getLoggingEvents().get(0).getLevel() == Level.DEBUG;
        assert appender.getLoggingEvents().get(0).getFormattedMessage() == "debug message";
        assert appender.getLoggingEvents().get(1).getLevel() == Level.INFO;
        assert appender.getLoggingEvents().get(1).getFormattedMessage() == "info message";
        assert appender.getLoggingEvents().get(2).getLevel() == Level.ERROR;
        assert appender.getLoggingEvents().get(2).getFormattedMessage() == "error message";
    }

}
