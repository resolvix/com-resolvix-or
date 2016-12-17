package com.bissons.or;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.bissons.or.btree.Key;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

import org.junit.Test;

/**
 *
 * @author rwbisson
 */
public class ObjectRepositoryBalancedTreeTestRig
{
    private class X
        implements com.bissons.or.btree.Key
    {
        private long m_iValue;

        public X(
                long p_Value
            )
        {
            m_iValue = p_Value;
            return;
        }

        public int compareTo(Key p_Operand)
        {
            X rX;
            if (p_Operand != null)
            {
                if (p_Operand instanceof X)
                {
                    rX = (X) p_Operand;
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


    private class Y
        implements com.bissons.or.btree.KeyFactory
    {
        public Key[] create(int p_nKey)
        {
            return new X[p_nKey];
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

        public Instruction(
                Long p_Id,
                Q p_Operand,
                Key p_Key
            )
        {
            m_Id = p_Id;
            m_Operand = p_Operand;
            m_Key = p_Key;
            return;
        }
    }


    private Instruction[] _generateInstructionFeed(
            long p_Seed,
            int p_Count
        )
    {
        int i, i_max;
        long j, k;
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
                    ax[i] = new Instruction(new Long(i), Q.ADD, new X(k));
                    break;

                case 1:
                case -1:
                    ax[i] = new Instruction(new Long(i), Q.DELETE, new X(k));
                    break;

                default:
                    System.out.println(i);
                    System.out.println(m);
            }
        }

        return ax;
    }



    public void go(boolean p_bEnableDelete, boolean p_bTestSpecificDelete)
    {
        int i, i_max, yy, zz;
        Instruction[] ax;

        Y kf = new Y();
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
            index.delete(new X(111)); // 3 instances down to 2
            index.delete(new X(121)); // 3 instances down to 2
            index.delete(new X(122)); // 6 instances down to 5
            index.delete(new X(134)); // 4 instances down to 3
            index.delete(new X(163)); // 2 instances down to 1
            index.delete(new X(165)); // 1 instances down to 0
            index.delete(new X(173)); // no instances; error
            index.delete(new X(229)); // 1 instances down to 0
            index.delete(new X(229)); // no instances; error
            index.delete(new X(255)); // 5 instances down to 4
            index.delete(new X(255)); // 4 instances down to 3
            index.delete(new X(255)); // 3 instances down to 2
            index.delete(new X(255)); // 2 instances down to 1
            index.delete(new X(255)); // 1 instance  down to 0
            index.delete(new X(255)); // no instances; error
            index.delete(new X(355)); // 1 instances down to 0
            index.delete(new X(388)); // 3 instances down to 2
            index.delete(new X(388)); // 2 instances down to 1
            index.delete(new X(388)); // 1 instances down to 0
            index.delete(new X(513)); // 3 instances down to 2
            index.delete(new X(513)); // 2 instances down to 1
            index.delete(new X(513)); // 1 instances down to 0
            index.delete(new X(529)); // 2 instances down to 1
            index.delete(new X(529)); // 1 instances down to 0
        }

        index.dump();

        return;
    }

    
    @Test
    public void test()
    {
        //ObjectRepositoryBalancedTreeTestRig tr;

        //tr = new ObjectRepositoryBalancedTreeTestRig();

        go(true, true);    
    }
}
