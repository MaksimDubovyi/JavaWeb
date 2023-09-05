package step.learning.filters;
import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;


@Singleton
public class RegistrationFilter implements Filter {
    private FilterConfig _filterConfig ;
    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig ;
    }

    public void doFilter(  ServletRequest servletRequest,  ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest ;
        HttpServletResponse response = (HttpServletResponse) servletResponse ;
        request.setCharacterEncoding( StandardCharsets.UTF_8.name() ) ;
        response.setCharacterEncoding( StandardCharsets.UTF_8.name() ) ;

        String requestMethod = request.getMethod();
        StringBuffer requestURL = request.getRequestURL();
        ioDemo(requestURL,requestMethod);


        filterChain.doFilter(servletRequest, servletResponse);

    }
    private void ioDemo(StringBuffer requestURL,String requestMethod)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());

        String fileContent = " - Поточна дата і час: "+formattedDate+"  - Метод запиту: "+requestMethod+" - URL запиту: "+requestURL+"\n";

        try(FileWriter writer =new FileWriter("RegistrationFilter.txt",true))
        {
            writer.write(fileContent);
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