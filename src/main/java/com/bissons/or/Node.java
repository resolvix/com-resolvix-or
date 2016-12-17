/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bissons.or;

/**
 *
 * @author rwbisson
 */
public class Node
{
    javax.jcr.Node m_Node;

    public Node(
            javax.jcr.Node p_Node
        )
    {
        m_Node = p_Node;
        return;
    }

    public void setProperty(
            String p_sName,
            java.io.InputStream p_InputStream
        )
    throws
            javax.jcr.ValueFormatException,
            javax.jcr.version.VersionException,
            javax.jcr.lock.LockException,
            javax.jcr.nodetype.ConstraintViolationException,
            javax.jcr.RepositoryException
    {
        javax.jcr.Property rJcrProperty;

        rJcrProperty = m_Node.setProperty(
                p_sName,
                p_InputStream
            );
        return;
    }

}
