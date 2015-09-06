package com.guidezup.server.dao;

import com.guidezup.server.entities.TokenEntity;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by VladS on 8/21/2015.
 */
@EnableTransactionManagement
public class TokenDaoImpl extends HibernateDaoSupport implements TokenDao
{
    @Transactional(readOnly = false)
    public void saveOrUpdate(TokenEntity tokenEntity)
    {
        getHibernateTemplate().saveOrUpdate(tokenEntity);
    }

    @Transactional(readOnly = true)
    public TokenEntity get(String id)
    {
        return getHibernateTemplate().get(TokenEntity.class, id);
    }

    @Transactional(readOnly = false)
    public void delete(String id)
    {
        TokenEntity token = getHibernateTemplate().get(TokenEntity.class, id);
        if (token != null)
        {
            getHibernateTemplate().delete(token);
        }

    }

    @Transactional(readOnly = false)
    public void delete24hoursBack()
    {
        String time24hoirsBack = "" + (System.currentTimeMillis()-(1000*60*60*24));
        List<TokenEntity> list = (List<TokenEntity>)getHibernateTemplate().find
                ("select token from TokenEntity token where token.timeStamp < " + time24hoirsBack);
        getHibernateTemplate().deleteAll(list);
    }
}
