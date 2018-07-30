package com.cjburkey.jbnge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Log {
    
    private static final Logger logger = LogManager.getLogger("jBNGE");
    
    public static void info(Object msg) {
        logger.info(sanitize(msg));
    }
    
    public static void info(Object msg, Object... args) {
        logger.info(sanitize(msg), args);
    }
    
    public static void warn(Object msg) {
        logger.warn(sanitize(msg));
    }
    
    public static void warn(Object msg, Object... args) {
        logger.warn(sanitize(msg), args);
    }
    
    public static void error(Object msg) {
        logger.error(sanitize(msg));
    }
    
    public static void error(Object msg, Object... args) {
        logger.error(sanitize(msg), args);
    }
    
    private static String sanitize(Object msg) {
        String out = (msg == null) ? "null" : msg.toString();
        return (out == null) ? "null" : out;
    }
    
    public static void exception(Throwable t) {
        error("An error has occurred: " + t.getMessage());
        error("A complete error report follows:");
        while (t != null) {
            printStackTrace(t);
            t = t.getCause();
        }
    }
    
    private static void printStackTrace(Throwable t) {
        error("  Exception: {}", t.getClass().getSimpleName());
        error("  Message: {}", t.getMessage());
        for (StackTraceElement e : t.getStackTrace()) {
            if (e != null) {
                error("    {}", e.toString());
            }
        }
    }
    
    public static void exception(Throwable t, boolean exit) {
        exception(t);
        if (exit) {
            GameEngine.instance.exit(false, true, false);
        }
    }
    
}