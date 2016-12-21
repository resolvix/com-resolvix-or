/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bissons.or;


import javax.naming.InitialContext;

import org.apache.jackrabbit.core.TransientRepository;

/**
 *
 * @author rwbisson
 */
public class Repository
{
    protected InitialContext m_InitialContext;

    private javax.jcr.Repository m_Repository;

    public Repository()
    {
        m_Repository = new TransientRepository();
        return;
    }

    public Session login()
    {
        javax.jcr.Session rJcrSession;
        Session rSession;

        try
        {
            rJcrSession = m_Repository.login();
            if (rJcrSession != null) {
                if (rJcrSession.isLive()) {
                    rSession = new Session(rJcrSession);
                    return rSession;
                }
            }
        }
        catch (Exception e)
        {
        }

        return null;
    }
}
