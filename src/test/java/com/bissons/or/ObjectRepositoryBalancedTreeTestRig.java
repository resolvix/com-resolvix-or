package com.bissons.or;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;
import com.bissons.or.btree.Key;
import com.bissons.or.btree.KeyFactory;
import org.junit.Test;

/**
 *
 * @author rwbisson
 */
public class ObjectRepositoryBalancedTreeTestRig
{
    private class LocalKey
        implements Key
    {
        private long m_iValue;

        public LocalKey(
            long p_Value
        ) {
            m_iValue = p_Value;
        }

        public int compareTo(Key p_Operand)
        {
            LocalKey rX;
            if (p_Operand != null)
            {
                if (p_Operand instanceof LocalKey)
                {
                    rX = (LocalKey) p_Operand;
                    return ((Long) m_iValue).compareTo(rX.m_iValue);
                }
            }
            else
            {
                return 1;
            }

            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String toString()
        {
            return Long.toString(m_iValue);

        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }


    }


    private class LocalKeyFactory
        implements KeyFactory
    {
        public Key[] create(int p_nKey)
        {
            return new LocalKey[p_nKey];
        }
    }
    
    private enum Q
    {
        ADD,
        DELETE
    }

    private class Instruction
    {
        Long m_Id;
        Q m_Operand;
        Key m_Key;

        Instruction(
                Long p_Id,
                Q p_Operand,
                Key p_Key
            )
        {
            m_Id = p_Id;
            m_Operand = p_Operand;
            m_Key = p_Key;
        }
    }


    private Instruction[] _generateInstructionFeed(
        long p_Seed,
        int p_Count
    ) {
        int i;
        int i_max;
        long j;
        long k;
        long m;
        Instruction x;
        Instruction[] ax = new Instruction[p_Count];

        Random r = new Random();
        r.setSeed(p_Seed);

        i_max = p_Count;
        for (i = 0; i < i_max; i++)
        {
            j = r.nextLong();
            if (j < 0) { j = j * -1; }

            k = (r.nextLong() % 1000);
            if (k < 0) { k = k * -1; }

            m = (j % 2);

            switch ((int) m)
            {
                case 0:
                    ax[i] = new Instruction(new Long(i), Q.ADD, new LocalKey(k));
                    break;

                case 1:
                case -1:
                    ax[i] = new Instruction(new Long(i), Q.DELETE, new LocalKey(k));
                    break;

                default:
                    System.out.println(i);
                    System.out.println(m);
                    break;
            }
        }

        return ax;
    }



    public void go(boolean p_bEnableDelete, boolean p_bTestSpecificDelete)
    {
        int i;
        int i_max;
        int yy;
        int zz;
        Instruction[] ax;

        LocalKeyFactory kf = new LocalKeyFactory();
        com.bissons.or.btree.Index index = new com.bissons.or.btree.Index(kf);
        

        //ax = _generateInstructionFeed(99, 3800000);
        ax = _generateInstructionFeed(105, 2000000);
        i_max = ax.length; yy = 0; zz = 0;
        for (i = 0; i < i_max; i++)
        {
            /*if (ax[i].m_Id == 227244 || ax[i].m_Id == 123463)
            {
                System.out.println("stop");
            }*/

            System.out.println("Instruction Id - ".concat(Long.toString(ax[i].m_Id)));
            switch (ax[i].m_Operand)
            {
                case ADD:
                    if (yy == 512) {
                        System.out.println("STOP");
                    }

                    System.out.println("");
                    System.out.println(yy);
                    index.insert(ax[i].m_Key, kf);
                    yy++;
                    break;

                case DELETE:
                    if (p_bEnableDelete) {
                        System.out.println("");
                        System.out.println(zz);
                        index.delete(ax[i].m_Key);
                        zz++;
                    }
                    break;

                default:
                    break;
            }

            try
            {
                index.checkIntegrity();
            }
            catch (Exception e)
            {
                System.out.println("Integrity check failure");
                System.out.println(e.getMessage());
            }
        }

        index.dump();

        if (p_bTestSpecificDelete)
        {
            index.delete(new LocalKey(111)); // 3 instances down to 2
            index.delete(new LocalKey(121)); // 3 instances down to 2
            index.delete(new LocalKey(122)); // 6 instances down to 5
            index.delete(new LocalKey(134)); // 4 instances down to 3
            index.delete(new LocalKey(163)); // 2 instances down to 1
            index.delete(new LocalKey(165)); // 1 instances down to 0
            index.delete(new LocalKey(173)); // no instances; error
            index.delete(new LocalKey(229)); // 1 instances down to 0
            index.delete(new LocalKey(229)); // no instances; error
            index.delete(new LocalKey(255)); // 5 instances down to 4
            index.delete(new LocalKey(255)); // 4 instances down to 3
            index.delete(new LocalKey(255)); // 3 instances down to 2
            index.delete(new LocalKey(255)); // 2 instances down to 1
            index.delete(new LocalKey(255)); // 1 instance  down to 0
            index.delete(new LocalKey(255)); // no instances; error
            index.delete(new LocalKey(355)); // 1 instances down to 0
            index.delete(new LocalKey(388)); // 3 instances down to 2
            index.delete(new LocalKey(388)); // 2 instances down to 1
            index.delete(new LocalKey(388)); // 1 instances down to 0
            index.delete(new LocalKey(513)); // 3 instances down to 2
            index.delete(new LocalKey(513)); // 2 instances down to 1
            index.delete(new LocalKey(513)); // 1 instances down to 0
            index.delete(new LocalKey(529)); // 2 instances down to 1
            index.delete(new LocalKey(529)); // 1 instances down to 0
        }

        index.dump();
    }

    
    @Test
    public void test()
    {
        //ObjectRepositoryBalancedTreeTestRig tr;

        //tr = new ObjectRepositoryBalancedTreeTestRig();

        go(true, true);    
    }
}
