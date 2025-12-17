package com.ticketoffice.backend.infra.adapters.out.db.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.TransactionConflictException;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Utility class for executing operations with retry logic.
 */
public class RetryUtil {
    private static final Logger logger = LoggerFactory.getLogger(RetryUtil.class);
    private static final Random RANDOM = new Random();

    /**
     * Default retry configuration
     */
    public static class RetryConfig {
        public static final int DEFAULT_MAX_RETRIES = 3;
        public static final long DEFAULT_INITIAL_BACKOFF_MS = 100; // 100ms
        public static final long DEFAULT_MAX_BACKOFF_MS = 5000;    // 5 seconds

        private final int maxRetries;
        private final long initialBackoffMs;
        private final long maxBackoffMs;

        public RetryConfig() {
            this(DEFAULT_MAX_RETRIES, DEFAULT_INITIAL_BACKOFF_MS, DEFAULT_MAX_BACKOFF_MS);
        }

        public RetryConfig(int maxRetries, long initialBackoffMs, long maxBackoffMs) {
            this.maxRetries = maxRetries;
            this.initialBackoffMs = initialBackoffMs;
            this.maxBackoffMs = maxBackoffMs;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public long getInitialBackoffMs() {
            return initialBackoffMs;
        }

        public long getMaxBackoffMs() {
            return maxBackoffMs;
        }
    }

    /**
     * Executes an operation with retry logic for transient failures.
     * Uses exponential backoff with jitter to prevent thundering herd problem.
     *
     * @param <T>            The return type of the operation
     * @param operationName  Name of the operation for logging
     * @param operation      The operation to execute
     * @param defaultValue   Default value to return if all retries fail
     * @param config         Retry configuration
     * @return The result of the operation or the default value if all retries fail
     */
    public static <T> T executeWithRetry(String operationName, 
                                       Supplier<T> operation, 
                                       T defaultValue,
                                       RetryConfig config) {
        int attempt = 0;
        long backoffTime = config.getInitialBackoffMs();
        Instant startTime = Instant.now();

        while (true) {
            attempt++;

            try {
                T result = operation.get();

                if (attempt > 1) {
                    logger.info("Operation {} succeeded after {} attempts ({} ms)",
                            operationName, attempt, Duration.between(startTime, Instant.now()).toMillis());
                }

                return result;

            } catch (Exception e) {
                if (!isRetryable(e) || attempt >= config.getMaxRetries()) {
                    logger.error("Operation {} failed after {} attempts: {}",
                            operationName, attempt, e.getMessage(), e);
                    return defaultValue;
                }

                // Calculate backoff with jitter
                long sleepTime = Math.min(backoffTime, config.getMaxBackoffMs());
                long jitter = (long) (RANDOM.nextDouble() * sleepTime * 0.2); // Add ±10% jitter
                sleepTime = sleepTime + jitter;

                logger.warn("Retryable error in {} (attempt {}/{}), retrying in {} ms: {}",
                        operationName, attempt, config.getMaxRetries(), sleepTime, e.getMessage());

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.error("Operation {} interrupted during backoff", operationName, ie);
                    return defaultValue;
                }

                // Exponential backoff
                backoffTime *= 2;
            }
        }
    }

    /**
     * Executes a void operation with retry logic for transient failures.
     *
     * @param operationName Name of the operation for logging
     * @param operation     The operation to execute
     * @param config        Retry configuration
     */
    public static void executeWithRetry(String operationName, 
                                      Runnable operation, 
                                      RetryConfig config) {
        executeWithRetry(operationName, () -> {
            operation.run();
            return null;
        }, null, config);
    }

    /**
     * Determines if an exception is retryable.
     *
     * @param e The exception to check
     * @return true if the operation should be retried, false otherwise
     */
    private static boolean isRetryable(Exception e) {
        return e instanceof AwsServiceException
                || e instanceof SdkClientException
                || e instanceof ProvisionedThroughputExceededException
                || e instanceof TransactionConflictException
                || (e.getCause() != null && isRetryable((Exception) e.getCause()));
    }
}
