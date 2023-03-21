package org.paytm.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paytm.entities.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfiguration
@ExtendWith(MockitoExtension.class)
public class AsyncConfigTest {

    @InjectMocks
    private AsyncConfig asyncConfig;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void asyncExecutor_success() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) asyncConfig.asyncExecutor();
        Assertions.assertNotNull(threadPoolTaskExecutor);

        assertEquals(30, threadPoolTaskExecutor.getCorePoolSize());
        assertEquals(30, threadPoolTaskExecutor.getMaxPoolSize());
        assertEquals(1000, threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity());
        assertEquals("Async-", threadPoolTaskExecutor.getThreadNamePrefix());
    }
}
