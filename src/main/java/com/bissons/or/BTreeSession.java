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
public class BTreeSession extends Session
{
    protected int bucketSize = 128;

    public BTreeSession(
            javax.jcr.Session p_Session
        )
    {
        super(p_Session);
        return;
    }


    private void _findNode(
            javax.jcr.Node p_rJcrNode,
            String p_sId
        )
    {
        boolean bFound;

        long i, i_max, j, k;

        javax.jcr.Node rJcrNode;
        javax.jcr.NodeIterator rJcrNodeIterator;

        

    }


    public void findItem(
            String p_sId
        )
    {
        javax.jcr.Node rJcrNode, rJcrNode_search;
        javax.jcr.NodeIterator rJcrNodeIterator;
        javax.jcr.Property propertySubNode;
        String sFirstId, sLastId;

        try
        {
            rJcrNode = m_Session.getRootNode();
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
                //rJcrNode = _findNode(rJcrNode, p_sId);
                //if (rJcrNode != null)
                {

                }
            //}
        }
        catch (javax.jcr.RepositoryException e)
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
