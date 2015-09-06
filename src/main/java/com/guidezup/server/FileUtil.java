package com.guidezup.server;

import com.guidezup.server.model.GuideFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.StringTokenizer;

/**
 * Created by VladS on 8/8/2015.
 */
public class FileUtil
{
    static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    @Autowired
    private ServerProperties sp;
    private String password;

    public FileUtil()
    {
    }


    public void deleteFilesOnSessionClose(HttpSession session)
    {
        String webAudioPath = session.getServletContext().getRealPath("/") + sp.getRelatedWebAudioLocation();
        log.debug("============== webAudioPath = " + webAudioPath);
        deleteFilesFromDir(webAudioPath, session.getId());
    }

    private void deleteFilesFromDir(String dirName, final String sessionId)
    {
        File dir = new File(dirName);
        File[] files = dir.listFiles(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.startsWith(sessionId);
            }
        });
        for (File file : files)
        {
            boolean res = file.delete();
            log.debug("deleteFilesFromDir " + file.getName() + " :" + res);

        }
    }

    public GuideFileName createAndGetRealFileNameToPlay(String audioFileName, HttpSession session)
    {
        String outputFileName = session.getId() + audioFileName;
//        String outputFileName = session.getId() + System.currentTimeMillis() + ".mp3";
        log.debug("============== outputFileName = " + outputFileName);
        String webAudioPath = session.getServletContext().getRealPath("/") + sp.getRelatedWebAudioLocation();
        log.debug("============== webAudioPath = " + webAudioPath);

        try
        {
            File e = new File(webAudioPath + "/" + outputFileName);
            if (!e.exists())
            {
                copyFile(new File(sp.getAudioLocation() + "/" + audioFileName), e);
            }
        }
        catch (IOException ex)
        {
            log.error(ex.getMessage(), ex);
        }
        GuideFileName fn = new GuideFileName();
        fn.setFileName(outputFileName);
        return fn;
    }

    public void deletePrevFile(String prevUrl, HttpSession session)
    {
        String webAudioPath = session.getServletContext().getRealPath("/") + sp.getRelatedWebAudioLocation();
        if (prevUrl != null && !prevUrl.contains("sample.mp3"))
        {
            try
            {
                String prevFileName = prevUrl.substring(prevUrl.lastIndexOf("/"));
                File prevFile = new File(webAudioPath + prevFileName);
                boolean res = prevFile.delete();
                log.debug("deleteFile " + prevFile.getName() + " :" + res);
                log.debug("============== prevFileName = " + prevFileName);
            }
            catch (Throwable t)
            {
                log.error(t.getMessage(), t);
            }
        }

    }

    private void copyFile(File sourceFile, File destFile) throws IOException
    {
        if (!destFile.exists())
        {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try
        {
            source = (new FileInputStream(sourceFile)).getChannel();
            destination = (new FileOutputStream(destFile)).getChannel();
            destination.transferFrom(source, 0L, source.size());
        }
        finally
        {
            if (source != null)
            {
                source.close();
            }
            if (destination != null)
            {
                destination.close();
            }
        }
    }


    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void createGuideFile(InputStream stream, String fileName)
    {
        try
        {
            OutputStream out = new FileOutputStream(new File(sp.getAudioLocation() + "/" + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            stream.close();
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
