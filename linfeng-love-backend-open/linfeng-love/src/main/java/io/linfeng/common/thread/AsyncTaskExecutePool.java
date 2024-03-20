/**
 * -----------------------------------
 *  Copyright (c) 2021-2024
 *  All rights reserved, Designed By linfeng.tech , linfengtech.cn
 *  林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 *  商业版授权联系技术客服	 QQ:  973921677/3582996245
 *  严禁分享、盗用、转卖源码或非法牟利！
 *  版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池装配类
 * @author JiangCX
 * @date 2023/2/20 22:04
 */
@Slf4j
@Configuration
public class AsyncTaskExecutePool implements AsyncConfigurer {

    /** 注入配置类 */
    private final AsyncTaskProperties config;

    public AsyncTaskExecutePool(AsyncTaskProperties config) {
        this.config = config;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(config.getCorePoolSize());
        //最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        //队列容量
        executor.setQueueCapacity(config.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix("lf-async-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("===="+throwable.getMessage()+"====", throwable);
            log.error("exception method:"+method.getName());
        };
    }
}
