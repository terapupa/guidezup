package com.guidezup.RWlockerTest;

/**
 * Created by VladS on 10/3/2015.
 */
public class TestWriter implements Runnable
{
    private final String name;
    private final TestReadWriteLocker locker;

    public TestWriter(String name, TestReadWriteLocker locker)
    {
        this.name = name;
        this.locker = locker;
    }

    public void run()
    {
        int count = 0;
        while (true)
        {
            locker.setValue(name + "@@@>" + count);
            count++;
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }

    public String getName()
    {
        return name;
    }
}
