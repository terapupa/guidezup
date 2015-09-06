package com.guidezup.server.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by VladS on 8/2/2015.
 */
@XmlRootElement(name = "labels")
public class Labels implements Serializable
{
    private String enterGuideNameLbl;
    private String searchLbl;
    private String locateLbl;
    private String buyTheGuideLbl;
    private String selectTheGuideLbl;
    private String mapLbl;
    private String streetViewLbl;
    private String paidLbl;
    private String directionsLbl;

    public String getEnterGuideNameLbl()
    {
        return enterGuideNameLbl;
    }

    public void setEnterGuideNameLbl(String enterGuideNameLbl)
    {
        this.enterGuideNameLbl = enterGuideNameLbl;
    }

    public String getSearchLbl()
    {
        return searchLbl;
    }

    public void setSearchLbl(String searchLbl)
    {
        this.searchLbl = searchLbl;
    }

    public String getLocateLbl()
    {
        return locateLbl;
    }

    public void setLocateLbl(String locateLbl)
    {
        this.locateLbl = locateLbl;
    }

    public String getBuyTheGuideLbl()
    {
        return buyTheGuideLbl;
    }

    public void setBuyTheGuideLbl(String buyTheGuideLbl)
    {
        this.buyTheGuideLbl = buyTheGuideLbl;
    }

    public String getSelectTheGuideLbl()
    {
        return selectTheGuideLbl;
    }

    public void setSelectTheGuideLbl(String selectTheGuideLbl)
    {
        this.selectTheGuideLbl = selectTheGuideLbl;
    }

    public String getMapLbl()
    {
        return mapLbl;
    }

    public void setMapLbl(String mapLbl)
    {
        this.mapLbl = mapLbl;
    }

    public String getStreetViewLbl()
    {
        return streetViewLbl;
    }

    public void setStreetViewLbl(String streetViewLbl)
    {
        this.streetViewLbl = streetViewLbl;
    }

    public String getPaidLbl()
    {
        return paidLbl;
    }

    public void setPaidLbl(String paidLbl)
    {
        this.paidLbl = paidLbl;
    }

    public String getDirectionsLbl()
    {
        return directionsLbl;
    }

    public void setDirectionsLbl(String directionsLbl)
    {
        this.directionsLbl = directionsLbl;
    }
}
