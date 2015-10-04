package com.guidezup.RWlockerTest;

/**
 * Created by VladS on 10/3/2015.
 */
public class TestRun
{
    private TestReadWriteLocker rwlocker = new TestReadWriteLocker();

    public void start()
    {
        Thread[] Readers = new Thread[10];
        Thread[] Writer = new Thread[10];
        for (int i = 0; i < 10; i++)
        {
            Readers[i] = new Thread(new TestReader(rwlocker), "Reader-" + i);
            Writer[i] = new Thread(new TestWriter("Writer-" + i, rwlocker), "Writer-" + i);
        }
        for (int i = 0; i < 10; i++)
        {
            Readers[i].start();
            Writer[i].start();
        }
    }

    public static void main(String[] arg)
    {
        new TestRun().start();
    }
}
