package org.tanfuhua.facade;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
@Slf4j
@Component
@AllArgsConstructor
public class ThreadPoolFacade {

    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(20,
            new ThreadFactoryBuilder()
                    .setNameFormat("Scheduled-Thread-%s")
                    .setUncaughtExceptionHandler(
                            (t, e) -> log.error(String.format("线程[%s]异常：%s", t.getName(), e.getMessage()), e))
                    .build());

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  TimeUnit unit) {
        return scheduledThreadPoolExecutor.schedule(command, initialDelay, unit);
    }

}
