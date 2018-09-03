package com.penguinwan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    public void writeLogMessage() {
        LOGGER.debug("debug message");
        LOGGER.info("info message");
        LOGGER.error("error message");
    }

}
