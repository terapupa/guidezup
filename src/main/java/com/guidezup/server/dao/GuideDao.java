package com.guidezup.server.dao;


import java.util.List;

import com.guidezup.server.entities.GuideEntity;
import com.guidezup.server.exception.DbObjectNotFoundException;
import com.guidezup.server.model.Language;

/**
 * Created by IntelliJ IDEA.
 * User: Vlad
 * Date: Aug 4, 2009
 * Time: 8:57:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GuideDao
{
    public Long addGuide(GuideEntity guide);

    public GuideEntity getGuide(long id);

    public void deleteGuide(long id);

    public void updateGuide(GuideEntity guide);

    public List<?> getAllPublishGuides();

    public List<GuideEntity> getPublishGuides(String language);

    public List<GuideEntity> searchPublishGuides(String value, String language);

    public int getTotalGuides();

    public void publishGuide(long guideId) throws DbObjectNotFoundException;

    public void unpublishGuide(long guideId) throws DbObjectNotFoundException;

    public List<GuideEntity> getPaidGuides(String language);


    /**
     * 
     * @return first element - number of guides, second - language
     */
    public List<Language> getGuideLanguages();

}
