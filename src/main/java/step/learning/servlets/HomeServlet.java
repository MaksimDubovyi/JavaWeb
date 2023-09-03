package step.learning.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {   // назва класу - довільна
    @Override
    protected void doGet(                    // назва методу - саме так (варації не допускаються)
                                             HttpServletRequest request,      // request - об'єкт, що надається веб-сервером
                                             HttpServletResponse response)    // response - те, що буде надіслано як відповідь
            throws ServletException, IOException {

        request.setAttribute(                // Атрибути - засіб передачі даних протягом запиту
                "pageName",                  // ключ - ім'я атрибуту (String)
                "home"                       // значення атрибуту (Object)
        );
        String text =
                request.getParameter(            // Параметри - query частина запиту (URL)
                        "text"                   // ...?text=Вітання, або дані форми (у doPost)
                ) ;                              // За відсутності параметру -- null
        request.setAttribute(
                "text",
                text
        ) ;
        request                              // робимо внутрішній редирект - передаємо роботу
                .getRequestDispatcher(           // до іншого обробника - ***.jsp
                        "WEB-INF/_layout.jsp" )  // для того щоб прибрати прямий доступ до ***.jsp його
                .forward( request, response ) ;  // переміщують у закриту папку WEB-INF
    }
}

/*
Servlets - спеціальні класи Java для мережевих задач
Робота з сервлетами вимагає встановлення servlet-api
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
Для веб-розробки частіше за все береться нащадок HttpServlet
HttpServlet - можна вважати аналогом контроллера у версі API

Після складання сервлет треба зареєструвати. Є два способи (без ІоС)
- через налаштування сервера web.xml
- за допомогою анотацій (servlet-api 3 та вище)

Через web.xml
 "+" централізовані декларації - усі в одному місці
     гарантований порядок декларацій
     сумісність зі старими технологіями
 "-" більш громіздкі інструкції

  <servlet>
    <servlet-name>Home</servlet-name>
    <servlet-class>step.learning.servlets.HomeServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>Home</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

 */