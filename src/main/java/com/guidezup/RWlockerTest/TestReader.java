package com.guidezup.RWlockerTest;

/**
 * Created by VladS on 10/3/2015.
 */
public class TestReader implements Runnable
{
    private final TestReadWriteLocker locker;

    public TestReader(TestReadWriteLocker locker)
    {
        this.locker = locker;
    }

    public void run()
    {
        while(true)
        {
            locker.getValue();
        }
    }

}
