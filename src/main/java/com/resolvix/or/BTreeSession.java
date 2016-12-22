/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.resolvix.or;

import javax.jcr.RepositoryException;

/**
 *
 * @author rwbisson
 */
public class BTreeSession extends Session
{
    protected int bucketSize = 128;

    public BTreeSession(
            javax.jcr.Session p_Session
        )
    {
        super(p_Session);
    }


    private void findNode(
            javax.jcr.Node p_rJcrNode,
            String p_sId
        )
    {
        boolean bFound;

        long i;
        long i_max;
        long j;
        long k;

        javax.jcr.Node rJcrNode;
        javax.jcr.NodeIterator rJcrNodeIterator;

        

    }


    public void findItem(
            String p_sId
        )
    {
        javax.jcr.Node rJcrNode;
        javax.jcr.Node rJcrNode_search;
        javax.jcr.NodeIterator rJcrNodeIterator;
        javax.jcr.Property propertySubNode;
        String sFirstId;
        String sLastId;

        try
        {
            rJcrNode = getRootNode();
            //propertySubNode = rJcrNode.getProperty("subNodes");
            //sFirstId = propertySubNode.getValues()Value().toString();
            //sLastId = propertyLastId.getValue().toString();

            //propertyLastId = rJcrNode.getProperty("last");

            /*if (sFirstId.compareTo(p_sId) < 0)
            {
                // p_sId is before sFirstId
            }
            else if (sLastId.compareTo(p_sId) > 0)
            {
                // p_sId is after sLastId
            }*/
            //else
            //{
                // p_sId is between sFirstId and sLastId
                //rJcrNode = findNode(rJcrNode, p_sId);
                //if (rJcrNode != null)
                {

                }
            //}
        }
        catch (RepositoryException e)
        {

        }
    }


    /*public insertItem(
            String p_sIdentifier
        )
    {
        javax.jcr.Node rJcrNode;

        try
        {
            rJcrNode = m_Session.getRootNode();




            
        }
        catch (javax.jcr.RepositoryException e)
        {


        }
    }*/
}
