/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bissons.or.btree;

import java.io.Externalizable;

/**
 *
 * @author rwbisson
 */
public interface Key extends Externalizable
{
    int compareTo(
        Key p_Operand
    );
}
