package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


import step.learning.services.dao.UserDao;
import step.learning.services.dao.WebTokenDao;
import step.learning.services.db.dto.User;
import step.learning.services.db.dto.WebToken;
import step.learning.services.kdf.KdfService;

@Singleton
public class InstallServlet extends HttpServlet {
    @Inject
    private UserDao userDao;

    @Inject
    private WebTokenDao webTokenDao;
    private final KdfService kdfService;
@Inject
    public InstallServlet(KdfService kdfService,WebTokenDao webTokenDao) {
        this.kdfService = kdfService;
        this.webTokenDao = webTokenDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{

            req.setAttribute("pageName", "install" ) ;
            req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward( req, resp ) ;
            userDao.install();
            webTokenDao.install();
            resp.getWriter().print("Install Ok");
        }
        catch (RuntimeException ex)
        {
            resp.getWriter().print("Install Error. Look at logs");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String salt= UUID.randomUUID().toString().substring(0,8);
            String passwordDk = kdfService.getDerivedKey(req.getParameter("input_passwordDk"),salt);

            String birthdateStr = req.getParameter("input_birthdate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthdate = dateFormat.parse(birthdateStr);

            User user = new User(
                    req.getParameter("input_firstName"),
                    req.getParameter("input_lastName"),
                    req.getParameter("input_email"),
                    req.getParameter("input_phone"),
                    birthdate,
                    req.getParameter("input_avatar"),req.getParameter("input_login"),
                    salt,
                    passwordDk,
                    new Date(),
                    req.getParameter("input_culture"),
                    req.getParameter("input_gender"));
            userDao.add(user);
        } catch (ParseException e) {
             e.printStackTrace();
        }

        resp.sendRedirect(req.getRequestURI()); //перенаправляємо на туж адресу, з якої надійшов new запит Цюадресу браузер перезапитує методо GET
    }

}
