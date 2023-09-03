package step.learning.filters;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class RegistrationFilter implements Filter {
    private FilterConfig _filterConfig ;
    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig ;
    }

    public void doFilter(  ServletRequest servletRequest,  ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
    {
        // за потреби роботи з request/response їх бажано перетворити до НТТР-
        HttpServletRequest request = (HttpServletRequest) servletRequest ;
        HttpServletResponse response = (HttpServletResponse) servletResponse ;
        request.setCharacterEncoding( StandardCharsets.UTF_8.name() ) ;
        response.setCharacterEncoding( StandardCharsets.UTF_8.name() ) ;
        /* Кодування request/response можна встановити ДО першого акту
         * читання/писання до них. Фільтр - ідеальне місце для цього */

        // спосіб передати дані про роботу фільтру - атрибути запиту
        servletRequest.setAttribute( "charset", StandardCharsets.UTF_8.name() ) ;


        String requestMethod = request.getMethod();
        StringBuffer requestURL = request.getRequestURL();
        ioDemo(requestURL,requestMethod);
        filterChain.doFilter(servletRequest, servletResponse);

    }
    private void ioDemo(StringBuffer requestURL,String requestMethod)
    {   Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentTime);

        String fileContent = " - Поточна дата і час: "+formattedDate+"  - Метод запиту: "+requestMethod+" - URL запиту: "+requestURL+"\n";
        String filename ="RegistrationFilter.txt";
        try(FileWriter writer =new FileWriter(filename,true))
        {
            writer.write(fileContent);
            System.out.println("Write success");
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        try(FileReader reader =new FileReader(filename); Scanner scanner =new Scanner(reader))
        {int x=0;
            while (scanner.hasNext()) {
                x++;
                System.out.println(x+") "+scanner.nextLine());
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    public void destroy() {
        _filterConfig = null ;
    }
}

/*
Д.З. Реалізувати послугу безпеки "Реєстрація", яка полягає у тому, що всі запити
до ресурсу реєструються (фіксуються на постійній основі)
- створити сервлетний фільтр
- зареєструвати його першим
- при кожному запиті дописувати (append) у файл-журнал (ім'я вибрати довільно)
   відомості: дата-час, метод запиту, URL запиту \n
Оскільки папка target не передається на Github, зробити скріншот вмісту файл-журналу
 */