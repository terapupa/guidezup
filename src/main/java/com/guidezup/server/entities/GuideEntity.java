package com.guidezup.server.entities;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: samoteso
 * Date: Aug 2, 2009
 * Time: 5:42:08 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name="guide")
@Entity
@Table(name = "tbl_guide")
public class GuideEntity implements Serializable
{

	public static final String NOT_DEFINED = "NOT DEFINED";

    @Id
    @GeneratedValue
    @Column(name="GUIDE_ID")
    private Long guideId;
    @Column(name="NAME", nullable=false)
    private String guideName = "";
    @Column(name="AUDIO_FILE_NAME")
    private String audioFile = "";
    @Column(name="LONGITUDE")
    private String longitude = "1";
    @Column(name="LATITUDE")
    private String latitude = "50";
    @Column(name="RATING")
    private int rating;
    @Column(name="SENSE_RADIUS")
    private int senseRadius = 50;
    @Column(name="PUBLISHED")
    private boolean published;
    @Column(name="LANGUAGE_VIEW")
    private String languageView;
    @Column(name="LANGUAGE")
    private String language;
    @Column(name="COUNTRY")
    private String country = NOT_DEFINED;
    @Column(name="DESCRIPTION")
    private String description = "";
    @Column(name="BUY_LINK")
    private String buyLink = "";


    public Long getGuideId()
    {
        return guideId;
    }

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
        Locale l = new Locale(language);
        setLanguageView(l.getDisplayLanguage());
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getGuideName()
    {
        return guideName;
    }

    public void setGuideName(String guideName)
    {
        this.guideName = guideName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAudioFile()
    {
        return audioFile;
    }

    public void setAudioFile(String audioFile)
    {
        this.audioFile = audioFile;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public int getSenseRadius()
    {
        return senseRadius;
    }

    public void setSenseRadius(int senseRadius)
    {
        this.senseRadius = senseRadius;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setPublished(boolean published)
    {
        this.published = published;
    }

    public String getLanguageView()
    {
        return languageView;
    }

    public void setLanguageView(String languageView)
    {
        this.languageView = languageView;
    }

    public String getBuyLink()
    {
        return buyLink;
    }

    public void setBuyLink(String buyLink)
    {
        this.buyLink = buyLink;
    }
}
