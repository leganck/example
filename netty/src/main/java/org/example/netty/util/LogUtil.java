package org.example.netty.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;

/**
 * 日志工具类
 *
 * @author leganck
 * @date 2021/4/28 9:00
 **/
public class LogUtil {
    public static final String LOGGER_NAME = "logger";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final Map<Level, String> levelMappings = new HashMap<>();

    static {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        Handler[] handlers = logger.getParent().getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                handler.setFormatter(new LogFormatter());
            }
        }
        logger.setLevel(Level.ALL);
    }

    static {
        levelMappings.put(Level.INFO, ANSI_GREEN);
        levelMappings.put(Level.WARNING, ANSI_YELLOW);
        levelMappings.put(Level.SEVERE, ANSI_RED);
    }

    private LogUtil() {

    }

    public static Logger getLogger() {
        return Logger.getLogger(LOGGER_NAME);
    }

    public static void main(String[] args) {
        getLogger().info("11231313");
    }

    static class LogFormatter extends Formatter {
        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            ThreadInfo threadInfo = ManagementFactory.getThreadMXBean().getThreadInfo(record.getThreadID());
            // date
            String s = ANSI_WHITE + "%s\t" +
                    // level
                    "%s%s " +
                    // thread name
                    ANSI_WHITE + "--- [%s] " +
                    // class name#function name
                    ANSI_CYAN + "%s " +
                    // message
                    ANSI_WHITE + " : %s" +
                    System.lineSeparator() + ANSI_RESET;
            return String.format(s, getFormatStr(record.getMillis()),
                    getLevelColor(record.getLevel()),
                    record.getLevel(),
                    threadInfo.getThreadName(),
                    record.getSourceClassName() + "#" + record.getSourceMethodName(),
                    record.getMessage());
        }

        private String getFormatStr(Long millis) {
            return dateTimeFormatter.format(
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(millis),
                            ZoneId.systemDefault()
                    )
            );
        }

        public String getLevelColor(Level level) {
            String color = levelMappings.get(level);
            return color == null ? ANSI_WHITE : color;

        }
    }


}
