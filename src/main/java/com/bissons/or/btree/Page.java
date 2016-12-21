/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bissons.or.btree;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author rwbisson
 */
public class Page
    implements Externalizable
{
    static final int DEFAULT_PAGE_K = 256;

    private int m_PageK;
    private boolean m_isLeaf;
    private Page m_pageParent;
    private Page[] m_apageChild;
    private int m_nKey;
    private Key[] m_aKey;

    private <T extends Key> Page(
        Page p_ParentPage,
        KeyFactory p_KeyFactory,
        int p_PageK
    ) {
        m_PageK = p_PageK;
        m_pageParent = p_ParentPage;
        m_apageChild = new Page[m_PageK * 2];
        m_aKey = p_KeyFactory.create(
                m_PageK * 2 - 1
            );
        m_isLeaf = true;
        return;
    }

    Page(
        Page p_ParentPage,
        KeyFactory p_KeyFactory
    ) {
        this(p_ParentPage, p_KeyFactory, DEFAULT_PAGE_K);
    }

    public Page getPage(
            int p_Index
        )
    {
        return m_apageChild[p_Index];
    }

    public Key getKey(
            int p_Index
        )
    {
        return m_aKey[p_Index];
    }

    private static SearchResult searchKey(
        Page p_Page,
        Key p_Key
    ) {
        int i, i_max, r;
        boolean bFound, bStop;
        SearchResult rSearchResult;
        i_max = p_Page.m_nKey;
        i = 0;

        rSearchResult = null;

        if (p_Page.m_isLeaf)
        {
            bFound = false;
            bStop = false;
            while (!bFound && !bStop)
            {
                if (i <= (p_Page.m_nKey - 1))
                {
                    if (p_Page.m_aKey[i] != null)
                    {
                        r = p_Key.compareTo(p_Page.m_aKey[i]);
                        if (r > 0) {
                            i++;
                        } else if (r == 0) {
                            bFound = true;
                        } else if (r < 0) {
                            bStop = true;
                        }
                    }
                }
                else
                {
                    bStop = true;
                }
            }

            if (bFound)
            {
                rSearchResult = new SearchResult(
                    p_Page,
                    i
                );
            }
        }
        else
        {
            bFound = false;
            bStop = false;
            while (!bFound && !bStop)
            {
                if (i < (p_Page.m_nKey - 1))
                {
                    if (p_Page.m_aKey[i] != null)
                    {
                        if (p_Key.compareTo(p_Page.m_aKey[i]) > 0) {
                            i++;
                        } else {
                            bFound = true;
                        }
                    }
                }
                else
                {
                    bStop = true;
                }
            }

            if (bFound && !bStop)
            {
                rSearchResult = searchKey(
                    p_Page.m_apageChild[i],
                    p_Key
                );

                if (rSearchResult == null && p_Key.compareTo(p_Page.m_aKey[i]) == 0)
                {
                    rSearchResult = new SearchResult(
                        p_Page,
                        i
                    );
                }
            }
        }

        return rSearchResult;
    }

    private void deleteKey(
        int p_iObjectIndex
    ) {
        int i, i_max;
        i_max = m_nKey - 1;
        for (i = p_iObjectIndex; i < i_max; i++) {
            m_aKey[i] = m_aKey[i + 1];
            if (!m_isLeaf) {
                m_apageChild[i] = m_apageChild[i + 1];
            }
        }

        m_aKey[i] = null;
        m_apageChild[i + 1] = null;
        m_nKey--;
    }

    private static void removeKey(
        Page p_Page,
        int p_Index
    ) {
        int i, i_max;
        i_max = p_Page.m_nKey;
        for (i = p_Index; i < i_max; i++) {
            p_Page.m_aKey[i] = p_Page.m_aKey[i + 1];
            p_Page.m_apageChild[i] = p_Page.m_apageChild[i + 1];
        }

        p_Page.m_aKey[i] = null;
        p_Page.m_apageChild[i + 1] = null;
        p_Page.m_nKey--;
    }


    private static void moveKey(
        Page p_Page_source,
        int p_Index_source,
        Page p_Page_target,
        int p_Index_target
    ) {
        p_Page_target.m_aKey[p_Index_target] = p_Page_source.m_aKey[p_Index_source];
    }

    private static void swapKey(
        Page p_Page_source,
        int p_Index_source,
        Page p_Page_target,
        int p_Index_target
    ) {
        Key tmpKey;
        tmpKey = p_Page_target.m_aKey[p_Index_target];
        p_Page_target.m_aKey[p_Index_target] = p_Page_source.m_aKey[p_Index_source];
        p_Page_source.m_aKey[p_Index_source] = tmpKey;
    }

    private static void deleteKey(
        Page p_Page,
        Key p_Key
    ) {
        int i, i_max;
        int objectIndex;
        Page parentPage;
        Page deletePage;
        Page leftPage;
        Page rightPage;
        Page prePage;

        SearchResult rSearchResult;

        System.out.println("deleteKey - m_nKey = ".concat(Integer.toString(p_Page.m_nKey)).concat(", ").concat(p_Key.toString()));

        if (p_Key.toString().compareTo("255") == 0) {
            System.out.println("stop");
        }

        rSearchResult = searchKey(
                p_Page,
                p_Key
            );

        if (rSearchResult == null) {
            System.out.println("deleteKey::rSearchResult == null");
            return;
        }

        deletePage = rSearchResult.getPage();
        if (deletePage == null) {
            System.out.println("deleteKey::deletePage == null");
            return;
        }

        objectIndex = rSearchResult.getIndex();

        //iObjectIndex = p_Page._getObjectIndex(p_lObjectId);

        //  1.  If the current page is a leaf page -
        if (deletePage.m_isLeaf)
        {
            //  1.a If the page is more than half full, the specified
            //      object may be deleted directly, otherwise page
            //      contents will need to be balanced between the relevant
            //      page and an adjacent page or merged and the relevant
            //      page deleted.
            if (deletePage.m_nKey > (deletePage.m_PageK - 1))
            {
                deletePage.deleteKey(objectIndex);
            }
            else
            {
                //  1.a.b.1 Iterate through the sibling pages attached to the
                //          parent page, locate the current page to determine
                //          its position.
                parentPage = deletePage.m_pageParent;
                if (parentPage != null)
                {
                    i = 0;
                    while (parentPage.m_apageChild[i] != deletePage) {
                        i++;
                    }

                    //  1.a.b.2 If the page position is the last one within the
                    //          parent page -
                    if (i == parentPage.m_nKey)
                    {
                        //  1.a.b.2.a.1 Get the page immediately to the left
                        //              of the current page.
                        leftPage = parentPage.m_apageChild[i - 1];

                        //  1.a.b.2.a.2 If the left page is more than half full,
                        //              move an object identifier from the left
                        //              page, to the parent page, and from the
                        //              parent page to the current page.
                        //
                        //              If the left page is less than half full,
                        //              the current page may be merged with the
                        //              left page.
                        if (leftPage.m_nKey > (leftPage.m_PageK - 1))
                        {
                            moveKey(parentPage, i, deletePage, objectIndex);
                            moveKey(leftPage, leftPage.m_nKey - 1, parentPage, i);
                            //deletePage.m_aObjectId[objectIndex] = parentPage.m_aObjectId[i];
                            //parentPage.m_aObjectId[i] = leftPage.m_aObjectId[leftPage.m_iObject - 1];
                            deleteKey(leftPage, leftPage.m_aKey[leftPage.m_nKey - 1]);
                        }
                        else
                        {
                            merge(leftPage, parentPage, deletePage);
                        }
                    }
                    else
                    {
                        // Notice of an error condition: for some reason, a page
                        // reference within a non-leaf page is duplicated. This
                        // means that the left page is in fact the same page as
                        // the right page for the purposes of the merge below.
                        // On iterating through the merge process, the present
                        // algoritm advances the m_nKey value for the left page
                        // which in turn means that the loop advances beyond the
                        // end of the array. In any event, merge of the same
                        // page is an error condition in itself.

                        //  1.a.b.2.b.1 Get the page immediately to the right
                        //              of the current page.
                        rightPage = parentPage.m_apageChild[i + 1];

                        //  1.a.b.2.b.2 If the right page is more than half full,
                        //              move an object identifier from the right
                        //              page, to the parent page, and from the
                        //              parent page to the current page.
                        //
                        //              If the right page is less than half full,
                        //              the current page may be merged with the
                        //              right page.
                        if (rightPage.m_nKey > rightPage.m_PageK - 1)
                        {
                            moveKey(parentPage, i, deletePage, objectIndex);
                            moveKey(rightPage, 0, parentPage, i);

                            //deletePage.m_aObjectId[objectIndex] = parentPage.m_aObjectId[i];
                            //parentPage.m_aObjectId[i] = rightPage.m_aObjectId[0];
                            deleteKey(rightPage, rightPage.m_aKey[0]);
                        }
                        else
                        {
                            merge(deletePage, parentPage, rightPage);
                        }
                    }
                }
            }
        }
        else
        {
            //  2.  Page is not a leaf page: to delete the specified object,
            //      we must ....
            prePage = deletePage.m_apageChild[objectIndex];

            //  Iterate through the balanced tree to locate the right-most
            //  page immediately [before?] the entry in the page in which
            //  the object persists.
            while (!prePage.m_isLeaf) {
                prePage = prePage.m_apageChild[prePage.m_nKey];
            }

            long objectId_tmp;

            //  Swap the leaf object, for the entry in the page.
            //objectId_tmp = prePage.m_aObjectId[prePage.m_iObject - 1];
            //prePage.m_aObjectId[prePage.m_iObject - 1] = deletePage.m_aObjectId[objectIndex];
            //deletePage.m_aObjectId[objectIndex] = objectId_tmp;
            swapKey(deletePage, objectIndex, prePage, prePage.m_nKey - 1);

            //  Delete the object from the leaf node.
            deleteKey(prePage, prePage.m_aKey[prePage.m_nKey - 1]);
        }

        System.out.println("deleteKey(2) - m_nKey = ".concat(Integer.toString(p_Page.m_nKey)));
    }


    private static void dump(
            Page p_Page,
            String p_Prefix
        )
    {
        int i, i_max;

        i_max = p_Page.m_nKey;
        if (p_Page.m_isLeaf)
        {
            for (i = 0; i < i_max; i++) {
                System.out.println(p_Prefix.toString().concat(p_Page.m_aKey[i].toString()));
            }
        }
        else
        {
            if (!p_Page.m_isLeaf)
            {
                for (i = 0; i < i_max; i++)
                {
                    dump(p_Page.m_apageChild[i], p_Prefix.concat(" - ").concat(Integer.toString(i)).concat(" - "));
                    System.out.println(p_Page.m_aKey[i].toString());
                }
                dump(p_Page.m_apageChild[i], p_Prefix.concat(" - ").concat(Integer.toString(i)).concat(" - "));
            }
        }

        return;
    }


/*
 * Need a higher level function to iterate through the tree to find the right
 * place to insert the relevant value.
 *
 */

    private static void insert(
        Page p_Page,
        Key p_Key,
        KeyFactory p_KeyFactory
    ) {
        int i;
        boolean bFound;
        Page rPage;

        rPage = p_Page;
        while (!rPage.m_isLeaf)
        {
            i = 0;
            bFound = false;
            while  (i < rPage.m_nKey && !bFound)
            {
                if (p_Key.compareTo(rPage.m_aKey[i]) <= 0) {
                    bFound = true;
                } else {
                    i++;
                }
            }
            rPage = rPage.m_apageChild[i];
        }

        insertX(rPage, p_Key, p_KeyFactory);
        if (p_Page.m_pageParent == null)
        {
            if (p_Page.m_nKey == (p_Page.m_PageK * 2 - 1))
            {
                splitPage(p_Page, p_KeyFactory);
            }
        }
    }

    private static void insertX(
        Page p_Page,
        Key p_Key,
        KeyFactory p_KeyFactory
    ) {
        int position;
        Page leftPage;
        Page rightPage;

        System.out.println("insert - m_nKey = ".concat(Integer.toString(p_Page.m_nKey)));
        if (p_Page.m_nKey == 23) {
            System.out.println("XXX");
        }

        if (p_Page.m_nKey == 0)
        {
            p_Page.m_nKey++;
            p_Page.m_aKey[0] = p_Key;
        }
        else
        {
            // locate position
            position = 0;
            while ((position < p_Page.m_nKey)
                    && (p_Key.compareTo(p_Page.m_aKey[position]) > 0)) {
                position++;
            }

            if (p_Page.m_nKey == (p_Page.m_PageK * 2 - 1))
            {
                rightPage = splitPage(p_Page, p_KeyFactory);
                if (position > (p_Page.m_PageK - 1))
                {
                    insert(rightPage, p_Key, p_KeyFactory);
                }
                else
                {
                    if (position != p_Page.m_nKey)
                    {
                        shiftKeysWithinPage(p_Page, position);
                    }
                    else
                    {
                        p_Page.m_nKey++;
                    }
                    p_Page.m_aKey[position] = p_Key;
                }
            }
            else
            {
                if (position != p_Page.m_nKey)
                {
                    shiftKeysWithinPage(p_Page, position);
                }
                else
                {
                    p_Page.m_nKey++;
                }
                p_Page.m_aKey[position] = p_Key;
            }
        }
    }


    private static void merge(
        Page p_LeftPage,
        Page p_ParentPage,
        Page p_RightPage
    ) {
        int i, j;
        int parentIndex;

        Page parentPage;
        Page mergePage;

        System.out.println("merge");
        System.out.println(p_LeftPage.toString());
        System.out.println(p_RightPage.toString());

        parentPage = p_ParentPage;
        parentIndex = 0;
        while (parentPage.m_apageChild[parentIndex] != p_RightPage) {
            parentIndex++;
        }

        //  Merge the right hand page into the left hand page.
        i = p_LeftPage.m_nKey;
        j = 0;
        while (j < p_RightPage.m_nKey) {
            p_LeftPage.m_aKey[i] = p_RightPage.m_aKey[j];
            p_LeftPage.m_nKey++;
            i++; j++;
        }

        //  Remove the right hand page from the parent page.
        removeKey(
                p_ParentPage,
                parentIndex
            );
    }


    private static void shiftKeysWithinPage(
        Page p_Page,
        int p_iStartPosition
    ) {
        int i;
        for (i = p_Page.m_nKey; i > p_iStartPosition; i--) {
            p_Page.m_aKey[i] = p_Page.m_aKey[i - 1];
            if (!p_Page.m_isLeaf) {
                p_Page.m_apageChild[i + 1] = p_Page.m_apageChild[i];
            }
        }

        p_Page.m_nKey++;
    }


    private static Page splitPage(
            Page p_Page,
            KeyFactory p_KeyFactory
        )
    {
        int i, position;
        Page rightPage;
        Page leftPage;

        System.out.println("splitPage");

        if (p_Page.m_nKey == (p_Page.m_PageK * 2 - 1))
        {
            rightPage = null;

            if (p_Page.m_pageParent == null)
            {
                //  1.  Split algorithm for the root page.
                leftPage = new Page(p_Page, p_KeyFactory);
                rightPage = new Page(p_Page, p_KeyFactory);

                System.out.println("splitPage - leftpage = ".concat(leftPage.toString()));
                System.out.println("splitPage - rightpage = ".concat(rightPage.toString()));

                for (i = 0; i < p_Page.m_PageK - 1; i++) {
                    leftPage.m_aKey[i] = p_Page.m_aKey[i];
                    rightPage.m_aKey[i] = p_Page.m_aKey[p_Page.m_PageK + i];
                }

                if (!p_Page.m_isLeaf)
                {
                    for (i = 0; i < p_Page.m_PageK; i++) {
                        leftPage.m_apageChild[i] = p_Page.m_apageChild[i];
                        leftPage.m_apageChild[i].m_pageParent = leftPage;
                        rightPage.m_apageChild[i] = p_Page.m_apageChild[p_Page.m_PageK + i];
                        rightPage.m_apageChild[i].m_pageParent = rightPage;
                    }

                    leftPage.m_isLeaf = false;
                    rightPage.m_isLeaf = false;
                }
                else
                {
                    p_Page.m_isLeaf = false;
                }

                p_Page.m_aKey[0] = p_Page.m_aKey[p_Page.m_PageK - 1];
                p_Page.m_nKey = 1;
                leftPage.m_nKey = p_Page.m_PageK - 1;
                rightPage.m_nKey = p_Page.m_PageK - 1;

                for (i = 1; i < p_Page.m_PageK * 2 - 1; i++) {
                    p_Page.m_aKey[i] = null;
                    p_Page.m_apageChild[i + 1] = null;
                }

                p_Page.m_apageChild[0] = leftPage;
                p_Page.m_apageChild[1] = rightPage;
            }
            else
            {
                //  2.  Split algorithm for non-root page.
                if (p_Page.m_pageParent.m_nKey == (p_Page.m_PageK * 2 - 1)) {
                    splitPage(p_Page.m_pageParent, p_KeyFactory);
                }

                //  2.1 Iterate through the parent page to locate the position
                //      at which the new page to be created by this splitPage()
                //      operation should be recorded.
                position = 0;
                /*while ((position < p_Page.m_pageParent.m_nKey)
                        && (p_Page.m_aKey[p_Page.m_PageK - 1].compareTo(p_Page.m_pageParent.m_aKey[position]) > 0)) {
                    position++;
                }*/
                while (position < p_Page.m_pageParent.m_nKey
                        && (p_Page != p_Page.m_pageParent.m_apageChild[position])) {
                    position++;
                }

                //  2.2 Shift the entries in the parent page to accomodate
                //      the new page reference.
                shiftKeysWithinPage(p_Page.m_pageParent, position);

                //  2.3 Move the object identifier at the centre point of the
                //      page being split to the newly created position in the
                //      parent page.
                moveKey(p_Page, p_Page.m_PageK - 1, p_Page.m_pageParent, position);
                //p_Page.m_pageParent.m_aObjectId[position] = p_Page.m_aObjectId[pageK - 1];

                //  2.4 Create a new page to subsist to the right of the page
                //      being split, and populate the new page with content
                //      from the page being split.
                rightPage = new Page(p_Page.m_pageParent, p_KeyFactory);
                System.out.println("splitPage - rightpage = ".concat(rightPage.toString()));
                for (i = 0; i < p_Page.m_PageK - 1; i++) {
                    rightPage.m_aKey[i] = p_Page.m_aKey[p_Page.m_PageK + i];
                }

                //  2.5 If the page being split is not a leaf page, move
                //      subordinate page references from the page being
                //      split to the newly created right page, and update
                //      the parent page link for those subordinate pages
                //      that have been moved.
                if (!p_Page.m_isLeaf) {
                    for (i = 0; i < p_Page.m_PageK; i++) {
                        rightPage.m_apageChild[i] = p_Page.m_apageChild[p_Page.m_PageK + i];
                        rightPage.m_apageChild[i].m_pageParent = rightPage;
                    }
                    rightPage.m_isLeaf = false;
                }
                
                //  2.6 Update object identified counts, and clear elements
                //      in the newly split page.
                p_Page.m_nKey = p_Page.m_PageK - 1;
                rightPage.m_nKey = p_Page.m_PageK - 1;
                for (i = 0; i < p_Page.m_PageK - 1; i++) {
                    p_Page.m_aKey[p_Page.m_PageK - 1 + i] = null;
                    p_Page.m_apageChild[p_Page.m_PageK + i] = null;
                }

                //  2.7 Update the parent page to reference the split page
                //      and the newly created right page.
                p_Page.m_pageParent.m_apageChild[position] = p_Page;
                p_Page.m_pageParent.m_apageChild[position + 1] = rightPage;
            }

            return rightPage;
        }
        else
        {
            return null;
        }
    }

    public void insert(
        Key p_Key,
        KeyFactory p_KeyFactory
    ) {
        insert(
                this,
                p_Key,
                p_KeyFactory
            );
        return;
    }


    public void delete(
                Key p_Key
        )
    {
        deleteKey(
                this,
                p_Key
            );
        return;
    }

    public void dump()
    {
        dump(this, "root - ");
        return;
    }

    static void serializeNodeStructure(
            Page p_Node
        )
    {


    }


    static void checkPageIntegrity(
            Page p_Page
        ) throws Exception
    {
        int i, i_max;
        if (!p_Page.m_isLeaf)
        {
            i_max = p_Page.m_nKey;
            for (i = 0; i < i_max; i++) {
                if (p_Page.m_apageChild[i].equals(p_Page.m_apageChild[i + 1])) {
                    throw new Exception("Integrity failure (".concat(p_Page.m_apageChild[i].toString()).concat(" / ").concat(
                            p_Page.m_apageChild[i + 1].toString()));
                }

                Page.checkPageIntegrity(p_Page.m_apageChild[i]);
            }

            Page.checkPageIntegrity(p_Page.m_apageChild[i]);
        }
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
