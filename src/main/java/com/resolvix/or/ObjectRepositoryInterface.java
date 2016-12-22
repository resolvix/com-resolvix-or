/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.resolvix.or;

/**
 *
 * @author rwbisson
 */
public interface ObjectRepositoryInterface
{
    public void insertObject(
            String p_sFilename,
            String p_sRepositoryURI
        );

    public void getObject(
            String p_sRepositoryURI
        );
}
