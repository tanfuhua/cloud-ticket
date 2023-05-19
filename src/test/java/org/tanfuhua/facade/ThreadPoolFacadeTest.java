package org.tanfuhua.facade;

import org.junit.Before;
import org.junit.Test;
import org.tanfuhua.util.ThreadUtil;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolFacadeTest {

    private ThreadPoolFacade threadPoolFacadeUnderTest;

    @Before
    public void setUp() {
        threadPoolFacadeUnderTest = new ThreadPoolFacade();
    }

    @Test
    public void testScheduleAtFixedRate() throws ExecutionException, InterruptedException {
        // Setup
        AtomicInteger i = new AtomicInteger();
        final Runnable command = new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date() + "哈哈:" + i.get() + ":" + this);
                if (i.get() >= 3) {
                    return;
                }
                i.incrementAndGet();
                threadPoolFacadeUnderTest.scheduleAtFixedRate(this, 2L,
                        TimeUnit.SECONDS);
            }
        };

        // Run the test
        final ScheduledFuture<?> result = threadPoolFacadeUnderTest.scheduleAtFixedRate(command, 2L,
                TimeUnit.SECONDS);

        // Verify the results
        ThreadUtil.sleep(10 * 1000);
    }
}
