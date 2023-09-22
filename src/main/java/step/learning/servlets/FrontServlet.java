package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.dao.WebTokenDao;
import step.learning.services.db.dto.User;
import step.learning.services.db.dto.WebToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Демонстрація роботи з фронтендом (SPA)
 */
@Singleton
public class FrontServlet extends HttpServlet {
    private final WebTokenDao webTokenDao;

    @Inject
    public FrontServlet(WebTokenDao webTokenDao) {
        this.webTokenDao = webTokenDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");
        if(authHeader==null)
        {//гостьовий режим
            resp.getWriter().print("Guest mode");
            return;
        }
         String pattern = "Bearer (.+)$";
         Pattern reges = Pattern.compile(pattern);
         Matcher matches = reges.matcher(authHeader);
         if(matches.find()){
             //токен переданий, перевіряємо його
             String token = matches.group(1);
             //декодуємо його з base64 до обєкту
             WebToken webToken;
           try{  webToken = new WebToken(token); }
           catch (ParseException ignored){
               resp.getWriter().print("Unparsable token "+ token) ;
               return;
           }
             //перевіряємо шляхом пошуку користувача
             User user=webTokenDao.getSubject(webToken);
             if(user==null)
             {
                 resp.getWriter().print("Invalid token "+ token);
             }
             resp.getWriter().print("Auth mode " +user.getFirstName());
             return;
         }
        resp.getWriter().print("Invalid authorization schema");
    }
}
