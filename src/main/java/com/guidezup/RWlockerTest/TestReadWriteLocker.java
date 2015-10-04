package com.guidezup.RWlockerTest;

/**
 * Created by VladS on 10/3/2015.
 */
public class TestReadWriteLocker
{
    private String value;
    private int read = 0;
    private boolean write;
    private int writeRequest = 0;


    private synchronized void lockRead()
    {
        while(write || writeRequest > 0)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        read++;
    }

    private synchronized void unlockRead()
    {
        read--;
        notifyAll();
    }

    private synchronized void lockWrite()
    {
        writeRequest++;
        while (read > 0 || write)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        writeRequest--;
        write = true;
    }

    private synchronized void unlockWrite()
    {
        write = false;
        notifyAll();
    }

    public String getValue()
    {
        lockRead();
        String val = value;
        System.out.println("current thread = " + Thread.currentThread().getName() + " read = " + read + " value = " + val);
        unlockRead();
        return val;
    }

    public void setValue(String value)
    {
        lockWrite();
        this.value = value;
        System.out.println("current thread = " + Thread.currentThread().getName() + " set value = " + this.value);
        unlockWrite();
    }


}
