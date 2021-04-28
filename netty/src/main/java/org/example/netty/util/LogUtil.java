package org.example.netty.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * 日志工具类
 *
 * @author leganck
 * @date 2021/4/28 9:00
 **/
public class LogUtil {
    public static final String LOGGER_NAME = "logger";

    static {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new LogFormatter());
        logger.addHandler(consoleHandler);
        //不向父记录器传递信息
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
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
        // Create a DateFormat to format the logger timestamp.
        private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            ThreadInfo threadInfo = ManagementFactory.getThreadMXBean().getThreadInfo(record.getThreadID());
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(threadInfo.getThreadName()).append("] ");
            builder.append(record.getLevel()).append(" ");
            builder.append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append(" - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }
    }
}
