package step.learning.filters;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CharsetFilter implements Filter {
    private FilterConfig _filterConfig ;
    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig ;
    }

    public void doFilter(                       // Метод роботи фільтру
                                                ServletRequest servletRequest,      // ! не НТТР-типи параметрів
                                                ServletResponse servletResponse,    // але реально це НТТР-дані.
                                                FilterChain filterChain             // Посилання на ланцюг
    ) throws IOException, ServletException {
        // за потреби роботи з request/response їх бажано перетворити до НТТР-
        HttpServletRequest request = (HttpServletRequest) servletRequest ;
        HttpServletResponse response = (HttpServletResponse) servletResponse ;
        request.setCharacterEncoding( StandardCharsets.UTF_8.name() ) ;
        response.setCharacterEncoding( StandardCharsets.UTF_8.name() ) ;
        /* Кодування request/response можна встановити ДО першого акту
         * читання/писання до них. Фільтр - ідеальне місце для цього */

        // спосіб передати дані про роботу фільтру - атрибути запиту
        servletRequest.setAttribute( "charset", StandardCharsets.UTF_8.name() ) ;

        System.out.println(
                _filterConfig.getServletContext().getRealPath("./")
        );

        // не забути - передати роботу по ланцюгу
        filterChain.doFilter(servletRequest, servletResponse);
        // код, записаний після "ланцюга" буде виконуватись на зворотному ході
    }

    public void destroy() {
        _filterConfig = null ;
    }
}

/* Фільтри (сервлетні фільтри)
Класи для мережевих задач, роль яких - утворення концепції Middleware -
утворення ланцюга об'єктів-виконавців з можливістю вставлення нових
об'єктів у цей ланцюг.
Ланцюг (в обробці веб-запиту) має "прямий" та "зворотний" хід, фільтри
мають змогу працювати у всіх "напрямах"
Фільтри, як правило, додають до багатьох маршрутів (часто - до всіх),
тоді як сервлети прив'язані до одного або групового маршруту (/user/*).
Задачі фільтрів - спільні дії, як-то встановлення кодування символів,
підключення до БД, перевірка авторизації, тощо
Особливість фільтрів ще й у тому, що вони не розділяються за методами
запиту

Опис фільтру не включає у процес, необхідна реєстрація
- або у web.xml
- або анотацією
Про переваги/недоліки - див. про сервлети, для фільтрів часто важливий порядок,
тому недолік анотацій щодо НЕгарантування порядку є критичним.
 */
/*
ServletContext - оточення, у якому працює сервлет - основа для визначення
адрес, як URL, так і файлових.

http://localhost:8080/JavaWeb_PU121/jsp?text=Hello
getContextPath() -- /JavaWeb_PU121       | контекстний шлях - база сайту
getRequestURI()  -- /JavaWeb_PU121/jsp   | повний URI (без параметрів)
getServletPath() -- /jsp                 | шлях - база маршруту сервлету - те,що у @WebServlet( "/jsp" )
getPathInfo()    -- null                 | варіативна частина шляху (*), якщо маршрут типу "/jsp/*"
getProtocol()    -- HTTP/1.1             |
getScheme()      -- http                 |
getRemoteHost()  -- 0:0:0:0:0:0:0:1      |
getQueryString() -- text=Hello           |

Щодо файлових шляхів:
(new File("./")).getAbsolutePath() -- C:\Servers\apache-tomcat-8.5.93\bin\.
   ==> наші коди виконуються через веб-сервер (tomcat), відповідно
       поточна папка "./" є робочою папкою ехе-файла сервера

getServletContext().getRealPath("./") -- ...\repos\JavaWeb-PU121\target\JavaWeb-PU121\
   ==> показує на папку деплою (target) - де розміщені зкомпільовані класи сервлету
аналоги
  - _filterConfig.getServletContext().getRealPath("./")
  - request.getServletContext().getRealPath("./")
 */
/*
Д.З. Реалізувати послугу безпеки "Реєстрація", яка полягає у тому, що всі запити
до ресурсу реєструються (фіксуються на постійній основі)
- створити сервлетний фільтр
- зареєструвати його першим
- при кожному запиті дописувати (append) у файл-журнал (ім'я вибрати довільно)
   відомості: дата-час, метод запиту, URL запиту \n
Оскільки папка target не передається на Github, зробити скріншот вмісту файл-журналу
 */