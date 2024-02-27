package com.li.wisdomcashier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadPoolConfig
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/12 17:01
 * @Version 1.0
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private Integer coreNum = Runtime.getRuntime().availableProcessors();

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(coreNum*2);
        // 设置最大线程数
        executor.setMaxPoolSize(coreNum*4);
        // 设置队列容量
        executor.setQueueCapacity(100);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("Thread-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduleExecutor(){
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(coreNum*2);
        // 设置队列容量
        executor.setMaximumPoolSize(coreNum*4);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setKeepAliveTime(60, TimeUnit.SECONDS);
        return executor;
    }
}
