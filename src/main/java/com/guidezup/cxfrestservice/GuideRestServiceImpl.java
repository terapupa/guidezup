package com.guidezup.cxfrestservice;

import com.guidezup.server.FileUtil;
import com.guidezup.server.ServerProperties;
import com.guidezup.server.ServerUtil;
import com.guidezup.server.dao.GuideDao;
import com.guidezup.server.dao.TokenDao;
import com.guidezup.server.entities.GuideEntity;
import com.guidezup.server.entities.TokenEntity;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.utils.multipart.AttachmentUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceContext;
import java.io.InputStream;
import java.util.List;

/**
 * Created by VladS on 7/23/2015.
 */
@Path("/")
@WebService(name = "guideRestService", targetNamespace = "http://www.guidezup.com")
public class GuideRestServiceImpl
{
    static final Logger log = LoggerFactory.getLogger(GuideRestServiceImpl.class);

    @Autowired
    private GuideDao guideDao;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ServerProperties serverProperties;
    @Resource
    private WebServiceContext context;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getGuide")
    public Response getGuide(@QueryParam("guideId") Long guideId)
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        if (guideId == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(guideDao.getGuide(guideId)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/searchPublishGuides")
    public Response searchPublishGuides(@QueryParam("language") String language, @QueryParam("pattern") String pattern)
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        if (language == null || language.isEmpty())
        {
            language = "English";
        }
        return Response.ok(guideDao.searchPublishGuides(pattern, language)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/getPublishGuides")
    public Response getPublishGuides(@QueryParam("language") String language)
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        if (language == null || language.isEmpty())
        {
            language = "English";
        }
        return Response.ok(guideDao.getPublishGuides(language)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/getPaidGuides")
    public Response getPaidGuides(@QueryParam("language") String language)
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        if (language == null || language.isEmpty())
        {
            language = "English";
        }
        return Response.ok(guideDao.getPaidGuides(language)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/getLanguages")
    public Response getLanguages()
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        return Response.ok(guideDao.getGuideLanguages()).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/getLables")
    public Response getLables(@QueryParam("language") String language)
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        return Response.ok(ServerUtil.getLabels(language)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/getFileToPlay")
    public Response getFileToPlay(@QueryParam("name") String name)
    {
        if (!isRequestValid())
        {
            return Response.ok("{}").build();
        }
        HttpSession session = getCurrentSession();
        log.debug("RS Session = " + session.getId());
        return Response.ok(fileUtil.createAndGetRealFileNameToPlay(name, session)).build();
    }

    private HttpSession getCurrentSession()
    {
        return getCurrentRequest().getSession(true);
    }

    private HttpServletRequest getCurrentRequest()
    {
        Message message = PhaseInterceptorChain.getCurrentMessage();
        return (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/init")
    public Response init()
    {
        if (serverProperties.isSecurityCheck())
        {
            String agent = getCurrentRequest().getHeader("User-Agent");
            String session = getCurrentSession().getId();
            long timestamp = System.currentTimeMillis();
            int token = (agent + session + timestamp).hashCode();
            HttpServletResponse httpResponse = (HttpServletResponse) PhaseInterceptorChain.getCurrentMessage().get(AbstractHTTPDestination.HTTP_RESPONSE);
            httpResponse.addCookie(new Cookie("token", "" + token));
            TokenEntity entity = new TokenEntity();
            entity.setSessionId(session);
            entity.setAgent(agent);
            entity.setTimeStamp(timestamp);
            entity.setToken(token);
            tokenDao.saveOrUpdate(entity);
            log.debug("init : token = " + token);
        }
//        return Response.ok("{}").build();
        return null;
    }

    @POST
    @Path("/addGuide")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addGuide(List<Attachment> atts)
    {
        String responseMessage = "file uploaded";
        String guideName = getAttachmentStringValue("guideName", atts);
        String country = getAttachmentStringValue("country", atts);
        String latitude = getAttachmentStringValue("latitude", atts);
        String longitude = getAttachmentStringValue("longitude", atts);
        String optRussian = getAttachmentStringValue("optRussian", atts);
        String buyLink = getAttachmentStringValue("buyLink", atts);
        String password = getAttachmentStringValue("password", atts);
        List<Attachment> list = AttachmentUtils.getMatchingAttachments("audioFile", atts);
        InputStream audioFile = null;
        if (!list.isEmpty())
        {
            audioFile = list.get(0).getObject(InputStream.class);
        }
        if (fileUtil.getPassword().equals(password))
        {
            if (guideName == null)
            {
                responseMessage = "ERROR : guideName is empty";
            }
            else if (country == null)
            {
                responseMessage = "ERROR : country is empty";
            }
            else if (latitude == null)
            {
                responseMessage = "ERROR : latitude is empty";
            }
            else if (longitude == null)
            {
                responseMessage = "ERROR : longitude is empty";
            }
            else if (audioFile == null)
            {
                responseMessage = "ERROR : audioFile is empty";
            }
            else
            {
                String language = "Russian";
                String l = "ru";
                if (optRussian == null)
                {
                    language = "English";
                    l = "en";
                }
                if (buyLink == null)
                {
                    buyLink = "FREE";
                }
                GuideEntity guide = new GuideEntity();
                guide.setGuideName(guideName);
                guide.setDescription(guideName);
                guide.setCountry(country);
                guide.setLatitude(latitude);
                guide.setLongitude(longitude);
                guide.setLanguage(l);
                guide.setLanguageView(language);
                guide.setBuyLink(buyLink);
                guide.setPublished(true);
                long guideId = guideDao.addGuide(guide);
                String fileName = "U3G" + guideId + "_guide.mp3";
                fileUtil.createGuideFile(audioFile, fileName);
                guide.setAudioFile(fileName);
                guideDao.updateGuide(guide);
                log.info("guideId = " + guideId);
            }
        }
        else
        {
            responseMessage = "ERROR : incorrect password";
        }
        return Response.ok(responseMessage).build();
    }

    private boolean isRequestValid()
    {
        if (!serverProperties.isSecurityCheck()) return true;
        boolean result = false;
        Cookie[] cookies = getCurrentRequest().getCookies();
        Integer token = null;
        if (cookies != null)
        {
            for (Cookie c : cookies)
            {
                if ("token".equals(c.getName()))
                {
                    token = Integer.parseInt(c.getValue());
                }
            }
        }
        if (token != null)
        {
            String sessionId = getCurrentSession().getId();
            TokenEntity entity = tokenDao.get(sessionId);
            if (entity != null && entity.getToken() == token)
            {
                String agent = getCurrentRequest().getHeader("User-Agent");
                int hash = (agent + sessionId + entity.getTimeStamp()).hashCode();
                if (hash == token)
                {
                    result = true;
                }
            }
        }
        log.debug("isRequestValid() = " + result);
//        todo - uncomment it!!!
        return result;
//        return true;
    }

    private String getAttachmentStringValue(String name, List<Attachment> atts)
    {
        String res = null;
        List<Attachment> list = AttachmentUtils.getMatchingAttachments(name, atts);
        if (!list.isEmpty())
        {
            res = list.get(0).getObject(String.class);
            if (res != null && res.trim().length() == 0)
            {
                res = null;
            }
        }
        return res;
    }

}
