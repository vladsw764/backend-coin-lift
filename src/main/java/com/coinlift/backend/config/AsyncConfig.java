package com.coinlift.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(NUM_THREADS); // Set the number of threads in the pool (adjust based on your requirements)
        executor.setMaxPoolSize(NUM_THREADS * 4); // Set the maximum number of threads in the pool (adjust based on your requirements)
        executor.setQueueCapacity(500); // Set the queue capacity for pending tasks (adjust based on your requirements)
        executor.setThreadNamePrefix("async-"); // Set a prefix for thread names (optional)
        executor.initialize();
        return executor;
    }
}
