package com.guidezup.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by VladS on 8/21/2015.
 */
@Entity
@Table(name = "tbl_token")
public class TokenEntity implements Serializable
{
    @Id
    private String sessionId;
    @Column(nullable=false)
    private long timeStamp;
    @Column(nullable=false)
    private String agent;
    @Column(nullable=false)
    private int token;

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getAgent()
    {
        return agent;
    }

    public void setAgent(String agent)
    {
        this.agent = agent;
    }

    public int getToken()
    {
        return token;
    }

    public void setToken(int token)
    {
        this.token = token;
    }
}
