
package com.guidezup.server.dao;

import com.guidezup.server.entities.GuideEntity;
import com.guidezup.server.exception.DbObjectNotFoundException;
import com.guidezup.server.model.Language;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Vlad
 * Date: Aug 4, 2009
 * Time: 8:56:13 PM
 * To change this template use File | Settings | File Templates.
 */
@EnableTransactionManagement
public class GuideDaoImpl extends HibernateDaoSupport implements GuideDao
{
    static final Logger log = LoggerFactory.getLogger(GuideDaoImpl.class);

    @Transactional(readOnly = false)
    public Long addGuide(GuideEntity guide)
    {
    	return (Long)getHibernateTemplate().save(guide);
    }

    @Transactional(readOnly = true)
    public GuideEntity getGuide(long id)
    {
        return getHibernateTemplate().get(GuideEntity.class, id);
    }

    @Transactional(readOnly = false)
    public void deleteGuide(long id)
    {
        GuideEntity guide = getHibernateTemplate().get(GuideEntity.class, id);
        getHibernateTemplate().delete(guide);
    }

    @Transactional(readOnly = false)
    public void updateGuide(GuideEntity guide)
    {
        getHibernateTemplate().update(guide);
    }

	@Transactional(readOnly = true)
    public List<GuideEntity> getAllPublishGuides()
    {
        return (List<GuideEntity>) getHibernateTemplate().find("select guide from GuideEntity guide where guide.published = 'true' order by guide.rating desc");
    }

    @Transactional(readOnly = true)
    public int getTotalGuides()
    {
        List<?> l = getHibernateTemplate().find("select count(guide) from GuideEntity guide");
        return (int)((Long)l.get(0)*1);
    }

    @Transactional
    public void publishGuide(long guideId) throws DbObjectNotFoundException
    {
        setPublish(guideId, true);
    }

    @Transactional
    public void unpublishGuide(long guideId) throws DbObjectNotFoundException
    {
        setPublish(guideId, false);
    }

    private void setPublish(long guideId, boolean status) throws DbObjectNotFoundException
    {
        GuideEntity guide = getHibernateTemplate().get(GuideEntity.class, guideId);
        if (guide == null)
        {
            throw new DbObjectNotFoundException("guide", guideId);
        }
        guide.setPublished(status);
        getHibernateTemplate().save(guide);
    }

    @Transactional(readOnly = true)
	public List<Language> getGuideLanguages()
	{
        List<Language> list = new ArrayList<Language>();
        List<String> result = (List<String>)getHibernateTemplate().find("select languageView from GuideEntity group by languageView");
        if (result != null && !result.isEmpty())
        {
            for (String s : result)
            {
                Language l = new Language();
                l.setLanguage(s);
                list.add(l);
            }
        }
        return list;
	}

    @Transactional(readOnly = true)
    public List<GuideEntity> getPublishGuides(String language)
    {
        String reguest = "select guide from GuideEntity guide where guide.published = 'true' and guide.languageView ='" +
                language + "' order by guide.rating desc";
        if (UNDEFINED.equalsIgnoreCase(language))
        {
            reguest = "select guide from GuideEntity guide where guide.published = 'true' order by guide.rating desc";
        }
        return (List<GuideEntity>) getHibernateTemplate().find(reguest);
    }

    @Transactional(readOnly = true)
    public List<GuideEntity> getPaidGuides(String language)
    {
        String reguest = "select guide from GuideEntity guide where guide.published='true' and guide.languageView='" +
                language + "' and guide.buyLink!='FREE' order by guide.rating desc";
        if (UNDEFINED.equalsIgnoreCase(language))
        {
            reguest = "select guide from GuideEntity guide where guide.published='true' and guide.buyLink!='FREE' order by guide.rating desc";
        }
        return (List<GuideEntity>) getHibernateTemplate().find(reguest);
    }

    @Transactional(readOnly = true)
    public List<GuideEntity> searchPublishGuides(String value, String language)
    {
        log.debug("search value = " + value);
        if ((value == null) || (value.length() == 0))
        {
            return getPublishGuides(language);
        }
        StringTokenizer st = new StringTokenizer(value, "!., ");
        List<Long>[] listArrayInt = new List[st.countTokens()];
        HashMap<Long, GuideEntity> entityMap = new HashMap<Long, GuideEntity>();
        for (int i = 0; i < listArrayInt.length; i++)
        {
            listArrayInt[i] = new ArrayList<Long>();
        }
        int index = 0;
        while(st.hasMoreTokens())
        {
            String token = st.nextToken().trim();
            List<GuideEntity> list = (List<GuideEntity>)getHibernateTemplate().find(getRequetsStringForSearch(language, token));
            for (GuideEntity ent : list)
            {
                listArrayInt[index].add(ent.getGuideId());
                entityMap.put(ent.getGuideId(), ent);
            }
            index++;
        }

        List<Long> intersection = listArrayInt[0];
        for (int i = 1; i < listArrayInt.length; i++)
        {
            intersection = ListUtils.intersection(intersection, listArrayInt[i]);
        }
        List<GuideEntity> res = new ArrayList<GuideEntity>();
        for (Long id : intersection)
        {
            res.add(entityMap.get(id));
        }
        return res;
    }

    private String getRequetsStringForSearch(String language, String token)
    {
        String reguest = "select guide from GuideEntity guide where guide.languageView ='" + language + "' and " +
                "(lower(guide.guideName) like '%" + token.toLowerCase() + "%' or lower(guide.description) like '%" +
                token.toLowerCase() + "%' or lower(guide.languageView) like '%" + token.toLowerCase() +
                "%' or lower(guide.country) like '%" + token.toLowerCase() + "%')  order by guide.rating desc";
        if (UNDEFINED.equalsIgnoreCase(language))
        {
            reguest = "select guide from GuideEntity guide where " +
                    "(lower(guide.guideName) like '%" + token.toLowerCase() + "%' or lower(guide.description) like '%" +
                    token.toLowerCase() + "%' or lower(guide.languageView) like '%" + token.toLowerCase() +
                    "%' or lower(guide.country) like '%" + token.toLowerCase() + "%')  order by guide.rating desc";
        }
        return reguest;
    }

}