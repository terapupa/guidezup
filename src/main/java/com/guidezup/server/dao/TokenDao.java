package com.guidezup.server.dao;

import com.guidezup.server.entities.TokenEntity;

/**
 * Created by VladS on 8/21/2015.
 */
public interface TokenDao
{
    public void saveOrUpdate(TokenEntity tokenEntity);

    public TokenEntity get(String id);

    public void delete(String id);

    public void delete24hoursBack();
}
