package com.guidezup.server;

import com.guidezup.server.model.Labels;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by VladS on 8/2/2015.
 */
public class ServerUtil
{
    private static ServerUtil instance = new ServerUtil();
    private ServerProperties serverProperties;

    private ServerUtil()
    {
    }

    public static ServerUtil getInstance()
    {
        return instance;
    }



    public static Labels getLabels(String language)
    {
        String locale = "en";
        if ("Russian".equalsIgnoreCase(language.trim()))
        {
            locale = "ru";
        }
        Locale l = new Locale(locale);
        ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", l);
        Labels lbl = new Labels();
        lbl.setBuyTheGuideLbl(labels.getString("buyTheGuideLbl"));
        lbl.setEnterGuideNameLbl(labels.getString("enterGuideNameLbl"));
        lbl.setLocateLbl(labels.getString("locateLbl"));
        lbl.setMapLbl(labels.getString("mapLbl"));
        lbl.setSearchLbl(labels.getString("searchLbl"));
        lbl.setSelectTheGuideLbl(labels.getString("selectTheGuideLbl"));
        lbl.setStreetViewLbl(labels.getString("streetViewLbl"));
        lbl.setPaidLbl(labels.getString("paidLbl"));
        lbl.setDirectionsLbl(labels.getString("directionsLbl"));
        return lbl;
    }

    public ServerProperties getServerProperties()
    {
        return serverProperties;
    }

    public void setServerProperties(ServerProperties serverProperties)
    {
        this.serverProperties = serverProperties;
    }

//    public static void main(String[] arg)
//    {
//        getLabels("Russian");
//    }
}
