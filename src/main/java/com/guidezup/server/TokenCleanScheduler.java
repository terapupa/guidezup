package com.guidezup.server;

import com.guidezup.server.dao.TokenDao;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by VladS on 8/22/2015.
 */
public class TokenCleanScheduler
{
    private static TokenCleanScheduler instance;

    private TokenCleanScheduler(final TokenDao tokenDao)
    {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable()
        {
            public void run()
            {
                tokenDao.delete24hoursBack();
            }
        }, 24, 24, TimeUnit.HOURS);
    }

    public static synchronized void start(TokenDao tokenDao)
    {
        if (instance == null)
        {
            instance = new TokenCleanScheduler(tokenDao);
        }
    }
}
