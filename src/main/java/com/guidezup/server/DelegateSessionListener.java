package com.guidezup.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by VladS on 8/8/2015.
 */
public class DelegateSessionListener implements HttpSessionListener
{
    static final Logger log = LoggerFactory.getLogger(DelegateSessionListener.class);
    private SessionListener sessionListener;

    public DelegateSessionListener()
    {
    }

    public void sessionCreated(HttpSessionEvent event)
    {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(event.getSession().getServletContext());
        sessionListener = context.getBean("sessionListener", SessionListener.class);
        sessionListener.sessionCreated(event);
    }

    public void sessionDestroyed(HttpSessionEvent event)
    {
        sessionListener.sessionDestroyed(event);
    }
}
