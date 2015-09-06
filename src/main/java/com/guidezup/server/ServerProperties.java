package com.guidezup.server;

/**
 * Created by VladS on 8/8/2015.
 */
public class ServerProperties
{
    private String audioLocation;
    private String relatedWebAudioLocation;
    private String relatedWebClientsLocation;
    private boolean securityCheck;

    public ServerProperties()
    {
        ServerUtil.getInstance().setServerProperties(this);
    }

    public String getAudioLocation()
    {
        return this.audioLocation;
    }

    public void setAudioLocation(String audioLocation)
    {
        this.audioLocation = audioLocation;
    }

    public String getRelatedWebAudioLocation()
    {
        return this.relatedWebAudioLocation;
    }

    public void setRelatedWebAudioLocation(String relatedWebAudioLocation)
    {
        this.relatedWebAudioLocation = relatedWebAudioLocation;
    }

    public String getRelatedWebClientsLocation()
    {
        return this.relatedWebClientsLocation;
    }

    public void setRelatedWebClientsLocation(String relatedWebClientsLocation)
    {
        this.relatedWebClientsLocation = relatedWebClientsLocation;
    }

    public boolean isSecurityCheck()
    {
        return securityCheck;
    }

    public void setSecurityCheck(boolean securityCheck)
    {
        this.securityCheck = securityCheck;
    }
}
