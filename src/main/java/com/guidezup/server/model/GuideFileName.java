package com.guidezup.server.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by VladS on 8/8/2015.
 */
@XmlRootElement(name = "guideFileName")
public class GuideFileName
{
    private String fileName;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
