package com.guidezup.server.exception;

/**
 * Created by IntelliJ IDEA.
 * User: samoteso
 * Date: Aug 6, 2009
 * Time: 5:47:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbObjectNotFoundException extends Exception
{
    public DbObjectNotFoundException(String s, long id)
    {
        super("DB object " + s + " with ID = " + id + " is not found.");
    }

}
