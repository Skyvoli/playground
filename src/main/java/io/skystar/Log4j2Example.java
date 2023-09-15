package io.skystar;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log4j2Example {

    private static final Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {
        logger.trace("Trace log message");
        logger.debug("Debug log message");
        logger.info("Info log message");
        logger.error("Error log message");
    }
}
