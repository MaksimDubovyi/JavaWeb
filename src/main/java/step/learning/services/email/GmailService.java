package step.learning.services.email;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GmailService implements EmailService{
    @Override
    public void send(Message message)
    {
        try{Transport.send(message);}
        catch (Exception ex){
            throw  new RuntimeException(ex);
        }

    }

    @Override
    public Message prepareMessage() {
        Properties emailProperties = new Properties() ;

        emailProperties.put( "mail.smtp.host", "smtp.gmail.com" ) ;
        emailProperties.put( "mail.smtp.auth", "true" ) ;
        emailProperties.put( "mail.smtp.starttls.enable", "true" ) ;
        emailProperties.put( "mail.smtp.port", "587" ) ;
        emailProperties.put( "mail.smtp.ssl.protocols", "TLSv1.2" ) ;
        emailProperties.put( "mail.smtp.ssl.trust", "smtp.gmail.com" ) ;

        javax.mail.Session mailSession = javax.mail.Session.getInstance(emailProperties,
                new Authenticator() {//засоби афвтентифікації закладаємо у сесію
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("girllittle257@gmail.com", "kopgatmtodpgflcb");
                    }
                }) ;

        MimeMessage message =new MimeMessage(mailSession);
        try {
            message.setFrom( new javax.mail.internet.InternetAddress(
                    "girllittle257@gmail.com" ) ) ;
            message.setSubject( "Вітання з сайту JavaWeb" ) ;
        }
        catch (Exception ex){
            throw  new RuntimeException(ex);
        }

                return message;
    }
}
