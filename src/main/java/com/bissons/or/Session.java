/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bissons.or;

import javax.jcr.RepositoryException;

/**
 *
 * @author rwbisson
 */
public class Session
{
    javax.jcr.Session m_Session;

    public Session(
            javax.jcr.Session p_Session
        )
    {
        m_Session = p_Session;
        return;
    }


    public Workspace getWorkspace()
    {
        javax.jcr.Workspace rJcrWorkspace;
        Workspace rWorkspace;

        rJcrWorkspace = m_Session.getWorkspace();
        if (rJcrWorkspace != null) {
            rWorkspace = new Workspace(rJcrWorkspace);
            return rWorkspace;
        }

        return null;
    }


    public Node insertItem(
            String p_sPath
        )
    {
        int i, i_max;
        String[] asPath;

        javax.jcr.Node rJcrNode;
        Node rNode;

        //  1.  Splice p_sPath apart, to obtain an array of node
        //      names.
        asPath = p_sPath.split("/");

        //  2.  Iterate through the spliced path to locate the
        //      relevant parent node.
        i_max = asPath.length;
        try
        {
            rJcrNode = m_Session.getRootNode();
            for (i = 0; i < i_max; i++)
            {
                try
                {
                    rJcrNode = rJcrNode.getNode(asPath[i]);
                }
                catch (javax.jcr.PathNotFoundException e)
                {
                    rJcrNode = rJcrNode.addNode(asPath[i]);
                }
            }

            if (rJcrNode != null) {
                if (rJcrNode != null) {
                    rNode = new Node(rJcrNode);
                    return rNode;
                }
            }
        }
        catch (javax.jcr.RepositoryException e)
        {
        }

        return null;
    }


    public void moveItem(
            String p_sPath_from,
            String p_sPath_to
        )
    throws
            javax.jcr.ItemExistsException,
            javax.jcr.PathNotFoundException,
            javax.jcr.version.VersionException,
            javax.jcr.nodetype.ConstraintViolationException,
            javax.jcr.RepositoryException
    {
        m_Session.move(
                p_sPath_from,
                p_sPath_to
            );
        return;
    }

    public void scratch()
    {
        javax.jcr.Node rJcrNode;
        javax.jcr.NodeIterator rJcrNodeIterator;
        javax.jcr.Workspace rJcrWorkspace;

        //rJcrNodeIterator = rJcrNode.getNodes();
        //rJcrWorkspace.
    }
}
