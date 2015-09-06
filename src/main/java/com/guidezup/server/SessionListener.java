/* ===========================================================================
 * Copyright (c) 2013 Comcast Corp. All rights reserved.
 * ===========================================================================
 *
 * Author: vsamot200
 * Created: 8/21/2015  10:15 AM
 */
package com.guidezup.server;

import com.guidezup.server.dao.TokenDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionListener implements HttpSessionListener
{
    static final Logger log = LoggerFactory.getLogger(SessionListener.class);

    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private TokenDao tokenDao;

    public void sessionCreated(HttpSessionEvent event)
    {
        log.info("sessionCreated Session = " + event.getSession().getId());
        TokenCleanScheduler.start(tokenDao);
    }

    public void sessionDestroyed(HttpSessionEvent event)
    {
        String sessionId = event.getSession().getId();
        log.info("sessionDestroyed Session = " + sessionId);
        fileUtil.deleteFilesOnSessionClose(event.getSession());
        tokenDao.delete(sessionId);
    }

}
