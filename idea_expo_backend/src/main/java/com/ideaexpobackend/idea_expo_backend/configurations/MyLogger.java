package com.ideaexpobackend.idea_expo_backend.configurations;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MyLogger {

    // Static constants for log configuration
    private static final String HIBERNATE_LOGGER = "org.hibernate";
    private static final String MYAPP_LOGGER = "com.ideaexpobackend";
    private static final String HIBERNATE_LOG_PATH = "Logs/HibernateLogs.log";
    private static final String MYAPP_LOG_PATH = "Logs/IdeaExpoLogs.log";
    private static final String FILE_LOG_PATTERN = "%d{dd-MMM-yyyy HH:mm:ss} || [%thread] || %-5level || %logger{36} || %file || %C{0} || %method || %line || %msg |||%n";
    private static final String CONSOLE_LOG_PATTERN = "%d{dd-MMM-yyyy HH:mm:ss} - [%thread] - %-5level - %logger{36} - %file - %line - %msg%n";


    @Bean
    public String configureCustomLoggers() {
        configureCustomLogger(MYAPP_LOGGER, Level.DEBUG, CONSOLE_LOG_PATTERN, FILE_LOG_PATTERN, MYAPP_LOG_PATH, "_%d{dd-MMM-yyyy}_%i.log.gz", "500MB", "10GB", 30);
        configureCustomLogger(HIBERNATE_LOGGER, Level.DEBUG, CONSOLE_LOG_PATTERN, FILE_LOG_PATTERN, HIBERNATE_LOG_PATH,"_%d{dd-MMM-yyyy}_%i.log.gz", "500MB", "10GB", 30);
        return "Loggers Configured";
    }

    public void configureCustomLogger(String loggerName, Level logLevel, String consolePattern, String filePattern, String filePath, String rollingFileSuffix, String maxSingleLogFileSize, String maxTotalArchiveFileSize, int archiveRetentionDurationInDays) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        ConsoleAppender<ILoggingEvent> consoleAppender = createConsoleAppender(context, consolePattern);
        RollingFileAppender<ILoggingEvent> fileAppender = createFileAppender(context, filePattern, filePath);

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = createRollingPolicy(context, fileAppender, filePath, rollingFileSuffix, maxSingleLogFileSize, maxTotalArchiveFileSize, archiveRetentionDurationInDays);
        rollingPolicy.start();
        fileAppender.setRollingPolicy(rollingPolicy);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(logLevel);
        logger.addAppender(consoleAppender);
        logger.addAppender(fileAppender);

        if(!loggerName.equals(Logger.ROOT_LOGGER_NAME))
            logger.setAdditive(false); // Prevent logging to root logger
    }

    private ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext context, String pattern) {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(createEncoder(context, pattern));
        consoleAppender.start();
        return consoleAppender;
    }

    private RollingFileAppender<ILoggingEvent> createFileAppender(LoggerContext context, String pattern, String filePath) {
        RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
        fileAppender.setContext(context);
        fileAppender.setFile(filePath);

        fileAppender.setEncoder(createEncoder(context, pattern));
        fileAppender.start();
        return fileAppender;
    }

    private PatternLayoutEncoder createEncoder(LoggerContext context, String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(pattern);
        encoder.start();
        return encoder;
    }

    private SizeAndTimeBasedRollingPolicy<ILoggingEvent> createRollingPolicy(LoggerContext context, RollingFileAppender<ILoggingEvent> fileAppender, String filePath, String rollingFileSuffix, String maxSingleLogFileSize, String maxTotalArchiveFileSize, int archiveRetentionDurationInDays) {
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setParent(fileAppender);
        rollingPolicy.setFileNamePattern(filePath + rollingFileSuffix);
        rollingPolicy.setMaxFileSize(FileSize.valueOf(maxSingleLogFileSize));
        rollingPolicy.setTotalSizeCap(FileSize.valueOf(maxTotalArchiveFileSize));
        rollingPolicy.setMaxHistory(archiveRetentionDurationInDays);
        rollingPolicy.start();
        return rollingPolicy;
    }
}