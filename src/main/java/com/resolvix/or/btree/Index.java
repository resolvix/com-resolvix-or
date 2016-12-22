/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.resolvix.or.btree;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author rwbisson
 */
public class Index
    implements Externalizable
{
    //
    //
    //
    private Page m_pageRoot;

    /**
     *
     * @param p_KeyFactory
     */
    public Index(
        KeyFactory p_KeyFactory
    ) {
        m_pageRoot = new Page(null, p_KeyFactory);
    }

    /**
     *
     * @throws Exception
     */
    public void checkIntegrity()
        throws Exception
    {
        Page.checkPageIntegrity(m_pageRoot);
    }

    /**
     *
     * @param p_Key
     * @param p_KeyFactory
     */
    public void insert(
        Key p_Key,
        KeyFactory p_KeyFactory
    ) {
        m_pageRoot.insert(p_Key, p_KeyFactory);
    }

    /**
     *
     * @param p_Key
     */
    public void delete(
        Key p_Key
    ) {
        m_pageRoot.delete(p_Key);
    }

    /**
     *
     */
    public void dump()
    {
        m_pageRoot.dump();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
