/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.resolvix.or.btree;

/**
 *
 * @author rwbisson
 */
public class SearchResult
{
    private Page m_Page;
    private int m_Index;

    public SearchResult(
            Page p_Page,
            int p_Index
        )
    {
        m_Page = p_Page;
        m_Index = p_Index;
        return;
    }

    public Page getPage()
    {
        return m_Page;
    }

    public int getIndex()
    {
        return m_Index;
    }
}
