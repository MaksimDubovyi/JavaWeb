package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.email.EmailService;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Properties;

@Singleton
public class MailServlet extends HttpServlet {
    private final EmailService emailService;
@Inject
    public MailServlet(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //метод викликається до того як відбувається розподіл за doXxxx методами,
        //тут є можливість вплинути на цей розподіл
        switch ((req.getMethod().toUpperCase()))
        {
            case "MAIL": this.doMail(req,resp);
            break;
            case "PATCH": this.doPatch(req,resp);
            break;
            case "LINK": this.doLink(req,resp);
                break;
            default:
                super.service(req,resp);//розподіл за замовчуванням
        }

    }

    protected void doLink(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = emailService.prepareMessage() ;
            message.setRecipients(  // Одержувачів також перекладаємо у повідомлення
                    Message.RecipientType.TO,
                    InternetAddress.parse( "maksim24du@gmail.com" ) ) ;
                    message.setContent( "<b>Вітаємо</b> з реєстрацією на <a href='https://javawebaa.azurewebsites.net'> сайті JavaWeb </a>!",
                    "text/html; charset=UTF-8" ) ;
            emailService.send( message ) ;
            resp.getWriter().print( "Service sent" ) ;
        }
        catch( Exception ex ) {
            resp.getWriter().print( ex.getMessage() ) ;
        }
    }
    protected void doMail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Properties emailProperties = new Properties() ;
        emailProperties.put( "mail.smtp.auth", "true" ) ;
        emailProperties.put( "mail.smtp.starttls.enable", "true" ) ;
        emailProperties.put( "mail.smtp.port", "587" ) ;
        emailProperties.put( "mail.smtp.ssl.protocols", "TLSv1.2" ) ;
        emailProperties.put( "mail.smtp.ssl.trust", "smtp.gmail.com" ) ;

        javax.mail.Session mailSession = javax.mail.Session.getInstance( emailProperties ) ;
        // mailSession.setDebug( true ) ;  // виводити у консоль процес надсилання пошти

        try( Transport emailTransport = mailSession.getTransport("smtp") ) {
            emailTransport.connect( "smtp.gmail.com", "girllittle257@gmail.com", "kopgatmtodpgflcb" ) ;
            // Налаштовуємо повідомлення
            javax.mail.internet.MimeMessage message = new MimeMessage( mailSession ) ;
            message.setFrom( new javax.mail.internet.InternetAddress( "girllittle257@gmail.com" ) ) ;
            message.setSubject( "From site JavaWeb" ) ;
            //message.setContent( "Вітаємо з реєстраціє на сайті!", "text/plain; charset=UTF-8" ) ;
            message.setContent( "<b>Вітаємо</b> з реєстраціє на <a href='https://javawebaa.azurewebsites.net'>сайті</a>!", "text/html; charset=UTF-8" );

            // Надсилаємо його
            emailTransport.sendMessage( message,
                    InternetAddress.parse( "maksim24du@gmail.com" ) ) ;
            resp.getWriter().print( "MAIL sent" ) ;
        }
        catch( MessagingException ex ) {
            resp.getWriter().print( ex.getMessage() ) ;
        }


    }
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // multipart повідомлення містить декілька частин з різним типом контенту
        // (для систем які не можуть відобпажати всі типи - вибирають для себе)
        //також демонструємо інший спосіб підключення
        Properties emailProperties = new Properties() ;
        emailProperties.put( "mail.smtp.auth", "true" ) ;
        emailProperties.put( "mail.smtp.starttls.enable", "true" ) ;
        emailProperties.put( "mail.smtp.port", "587" ) ;
        emailProperties.put( "mail.smtp.ssl.protocols", "TLSv1.2" ) ;
        emailProperties.put( "mail.smtp.host", "smtp.gmail.com" ) ;
        emailProperties.put( "mail.smtp.ssl.trust", "smtp.gmail.com" ) ;

        javax.mail.Session mailSession = javax.mail.Session.getInstance(emailProperties,
                new Authenticator() {//засоби фвтентифікації закладаємо у сесію
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("girllittle257@gmail.com", "kopgatmtodpgflcb");
            }
        }) ;

        MimeMessage message =new MimeMessage(mailSession);
        try{
            message.setFrom( new javax.mail.internet.InternetAddress( "girllittle257@gmail.com" ) ) ;
            message.setSubject( "Вітання з сайту JavaWeb" ) ;
            message.setRecipients(//Одержувачів також перекладаємо у повідомлення
                    Message.RecipientType.TO,
                    InternetAddress.parse("maksim24du@gmail.com"));//можна додати масив адрес і буде всім відправленно
            //Формуємо частини
            MimeBodyPart htmlPart=new MimeBodyPart();//html - для пристроїв що можуть їх показувати
            htmlPart.setContent("<b>Вітаємо</b> з реєстраціє на <a href='https://javawebaa.azurewebsites.net'>сайті</a>!", "text/html; charset=UTF-8");

            MimeBodyPart textPart=new MimeBodyPart();// Текст для простіших пристроїв
            textPart.setContent("Вітаємо з реєстраціє на сайті!", "text/plain; charset=UTF-8");

            //файлова частина (attachment)
            String resurceName="car.jpg";
            //звертаємось до ресурсу у папці target/classes та визначаємо його шлях
            String resurcePath=this.getClass().getClassLoader().getResource(resurceName).getPath() ;
            //якщо шлях до ресурсу містить спец символи то вони будуть URL-кодовані (пробіл -%20 і т.д.) Декодуємо їх
                    try{resurcePath=URLDecoder.decode(resurcePath,"UTF-8");}
                    catch (Exception ignored){}

            MimeBodyPart filePart = new MimeBodyPart();
            filePart.setDataHandler(new DataHandler(new FileDataSource(resurcePath)));
            filePart.setFileName(resurceName);

            //формуємо зібраний контент
            Multipart mailContent= new MimeMultipart();
            mailContent.addBodyPart(textPart);
            mailContent.addBodyPart(htmlPart);
            mailContent.addBodyPart(filePart);
            //передаємо його у відповідь
            message.setContent(mailContent);
            //надсилаємо його статичним методом
            Transport.send(message);
            resp.getWriter().print("Multipart send");
        }
        catch( MessagingException ex ) {
            resp.getWriter().print( ex.getMessage() ) ;
        }
        // mailSession.setDebug( true ) ;  // виводити у консоль процес надсилання пошти
      resp.getWriter().print("PATCH works");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageName", "email" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward( req, resp ) ;
    }
}
/*
Робота з електронною поштою

Оскільки надсилання пошти є потенційно вразливим інструментом, реалізуємо його
нестандартним методом запиту "MAIL"
 */