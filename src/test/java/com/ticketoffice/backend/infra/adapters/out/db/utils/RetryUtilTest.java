package com.ticketoffice.backend.infra.adapters.out.db.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.TransactionConflictException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class RetryUtilTest {
    private RetryUtil.RetryConfig testConfig;
    private final AtomicInteger attemptCounter = new AtomicInteger(0);

    @BeforeEach
    void setUp() {
        // Reset counter before each test
        attemptCounter.set(0);

        // Create a test config with shorter times for testing
        testConfig = new RetryUtil.RetryConfig(3, 10, 100);
    }

    @Test
    void executeWithRetry_SuccessOnFirstAttempt() {
        // Arrange
        String expected = "success";
        Supplier<String> operation = () -> expected;

        // Act
        String result = RetryUtil.executeWithRetry("testOperation", operation, "default", testConfig);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void executeWithRetry_RetryAndSucceed() {
        // Arrange
        String expected = "success";
        Supplier<String> operation = () -> {
            if (attemptCounter.incrementAndGet() < 2) {
                throw SdkClientException.create("Temporary failure", new RuntimeException());
            }
            return expected;
        };

        // Act
        String result = RetryUtil.executeWithRetry("testOperation", operation, "default", testConfig);

        // Assert
        assertEquals(expected, result);
        assertEquals(2, attemptCounter.get());
    }

    @Test
    void executeWithRetry_ExhaustRetries() {
        // Arrange
        String errorMessage = "Persistent failure";
        Supplier<String> operation = () -> {
            attemptCounter.incrementAndGet();
            throw SdkClientException.create(errorMessage, new RuntimeException());
        };

        // Act
        String result = RetryUtil.executeWithRetry("testOperation", operation, "default", testConfig);

        // Assert
        assertEquals("default", result);
        assertEquals(testConfig.getMaxRetries(), attemptCounter.get());
    }

    @Test
    void executeWithRetry_NonRetryableException() {
        // Arrange
        String errorMessage = "Non-retryable error";
        Supplier<String> operation = () -> {
            attemptCounter.incrementAndGet();
            throw new IllegalArgumentException(errorMessage);
        };

        // Act
        String result = RetryUtil.executeWithRetry("testOperation", operation, "default", testConfig);

        // Assert
        assertEquals("default", result);
        assertEquals(1, attemptCounter.get());
    }

    @Test
    void executeWithRetry_InterruptedException() {
        // Arrange
        Supplier<String> operation = () -> {
            attemptCounter.incrementAndGet();
            Thread.currentThread().interrupt();
            throw SdkClientException.create("Interrupted", new InterruptedException());
        };

        // Act
        String result = RetryUtil.executeWithRetry("testOperation", operation, "default", testConfig);

        // Assert
        assertEquals("default", result);
        assertTrue(Thread.interrupted(), "Thread interrupt flag should be cleared");
        assertEquals(1, attemptCounter.get());
    }

    @Test
    void executeVoidWithRetry_Success() {
        // Arrange
        AtomicInteger counter = new AtomicInteger(0);
        Runnable operation = counter::incrementAndGet;

        // Act
        RetryUtil.executeWithRetry("testVoidOperation", operation, testConfig);

        // Assert
        assertEquals(1, counter.get());
    }

    @Test
    void executeVoidWithRetry_RetryAndSucceed() {
        // Arrange
        AtomicInteger counter = new AtomicInteger(0);
        Runnable operation = () -> {
            if (counter.incrementAndGet() < 2) {
                throw SdkClientException.create("Temporary failure", new RuntimeException());
            }
        };

        // Act
        RetryUtil.executeWithRetry("testVoidOperation", operation, testConfig);

        // Assert
        assertEquals(2, counter.get());
    }

    @Test
    void retryConfig_DefaultValues() {
        // Arrange & Act
        RetryUtil.RetryConfig defaultConfig = new RetryUtil.RetryConfig();

        // Assert
        assertEquals(3, defaultConfig.getMaxRetries());
        assertEquals(100, defaultConfig.getInitialBackoffMs());
        assertEquals(5000, defaultConfig.getMaxBackoffMs());
    }

    @Test
    void retryConfig_CustomValues() {
        // Arrange & Act
        RetryUtil.RetryConfig customConfig = new RetryUtil.RetryConfig(5, 50, 1000);

        // Assert
        assertEquals(5, customConfig.getMaxRetries());
        assertEquals(50, customConfig.getInitialBackoffMs());
        assertEquals(1000, customConfig.getMaxBackoffMs());
    }

    @Test
    void isRetryable_AwsServiceException() {
        // This test verifies the isRetryable method via reflection
        AwsServiceException exception = (AwsServiceException) AwsServiceException.builder()
                .message("Test exception")
                .statusCode(500)
                .build();
        assertTrue(isRetryableViaReflection(exception));
    }

    @Test
    void isRetryable_SdkClientException() {
        SdkClientException exception = SdkClientException.create("Test exception", new RuntimeException());
        assertTrue(isRetryableViaReflection(exception));
    }

    @Test
    void isNotRetryable_RuntimeException() {
        assertFalse(isRetryableViaReflection(new RuntimeException("Test exception")));
    }

    @Test
    void isRetryable_ProvisionedThroughputExceeded() {
        ProvisionedThroughputExceededException exception = ProvisionedThroughputExceededException.builder()
                .message("Provisioned throughput exceeded")
                .build();
        assertTrue(isRetryableViaReflection(exception));
    }

    @Test
    void isRetryable_TransactionConflict() {
        TransactionConflictException exception = TransactionConflictException.builder()
                .message("Transaction conflict")
                .build();
        assertTrue(isRetryableViaReflection(exception));
    }

    @Test
    void isRetryable_NestedRetryableException() {
        Exception nested = SdkClientException.create("Nested exception", new RuntimeException());
        Exception wrapper = new RuntimeException("Wrapper exception", nested);
        assertTrue(isRetryableViaReflection(wrapper));
    }

    // Helper method to test the private isRetryable method via reflection
    private boolean isRetryableViaReflection(Throwable throwable) {
        try {
            java.lang.reflect.Method method = RetryUtil.class.getDeclaredMethod("isRetryable", Exception.class);
            method.setAccessible(true);
            return (boolean) method.invoke(null, throwable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke isRetryable via reflection", e);
        }
    }
}