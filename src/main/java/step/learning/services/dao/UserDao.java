//package step.learning.db.dao;
package step.learning.services.dao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.services.db.DbProvider;
import step.learning.services.db.dto.User;
import step.learning.services.kdf.KdfService;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserDao {
    private final DbProvider dbProvider ;
    private final KdfService kdfService;
    private final Logger logger;
    private  final String dbPrefix;
    @Inject
    public UserDao(DbProvider dbProvider, KdfService kdfService, Logger logger,@Named("DbPrefix")String dbPrefix) {
        this.dbProvider = dbProvider;
        this.kdfService = kdfService;
        this.logger = logger;
        this.dbPrefix=dbPrefix;
    }


    /**
     * CREATE TABLE and INSERT first user
     */
    public void install() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS "+dbPrefix+"Users (" +
                "id                 CHAR(36) PRIMARY KEY," +
                "firstName        VARCHAR(50)  NULL," +
                "lastName         VARCHAR(50)  NULL," +
                "email            VARCHAR(128)  NOT NULL," +
                "emailConfirmCode CHAR(6) NULL," +
                "phone            VARCHAR(16) NULL," +
                "phoneConfirmCode CHAR(6) NULL," +
                "birthdate        DATETIME     NULL," +
                "avatar           VARCHAR(512) NULL," +
                "`login`          VARCHAR(64) NOT NULL UNIQUE," +
                "salt             CHAR(8) NOT NULL," +
                "passwordDk       VARCHAR(64) NOT NULL COMMENT 'Derived Key (RFC 2898)'," +
                "registerDT       DATETIME     DEFAULT  CURRENT_TIMESTAMP," +
                "lastLoginDT      DATETIME     NULL," +
                "culture          CHAR(5)      NULL COMMENT 'uk-UA'," +
                "gender           VARCHAR(64) NULL," +
                "banDT            DATETIME     NULL," +
                "deleteDT         DATETIME     NULL," +
                "roleId           CHAR(36)     NULL" +
                ") Engine = InnoDB  DEFAULT CHARSET = utf8";

        String id = "701ec766-5145-11ee-8eed-de2c4707f29e";
        String salt= UUID.randomUUID().toString().substring(0,8);
        String defaultPassword="123";
        String passwordDk = kdfService.getDerivedKey(defaultPassword,salt);
        String insertSQL = String.format(
                "INSERT INTO %1$sUsers (id, email, `login`, salt, passwordDk) " +
                        "VALUES( '%2$s', 'admin@some.mail.com', 'admin', '%3%s', '%4$s' ) " +
                        "ON DUPLICATE KEY UPDATE salt = '%3$s', passwordDk = '%4$s' ",
                dbPrefix, id, salt, passwordDk
        ) ;

        try(Statement statement=dbProvider.getConnection().createStatement())
        {
            statement.executeUpdate(createTableSQL);
            statement.execute(insertSQL);
        }
        catch (SQLException ex)
        {
            logger.log(Level.SEVERE,ex.getMessage()+"--"+"createTableSQL"+"--"+insertSQL);
            throw new RuntimeException(ex);
        }

    }

    public void add( User user )
    {
        String sql = "INSERT INTO " + dbPrefix + "Users(`id`, `firstName`, `lastName`,`email`," +
                " `phone`,`birthdate`,`avatar`,`login`,`salt`,`passwordDk`,`registerDT`,`culture`,`gender`, " +
                "`emailConfirmCode`, `phoneConfirmCode`)" +
                " VALUES( ?, ?, ?, ?,?,?,?,?,?, ?, ?, ?,?,?,? )";
        try( PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql) ) {

            prep.setString(1, user.getId()==null ? UUID.randomUUID().toString() : user.getId().toString());
            prep.setString(2, user.getFirstName());
            prep.setString(3, user.getLastName());
            prep.setString(4, user.getEmail());
            prep.setString(5, user.getPhone());
            prep.setDate (6, new java.sql.Date(user.getBirthdate().getTime()));
            prep.setString(7, user.getAvatar());
            prep.setString(8, user.getLogin());
            prep.setString(9, user.getSalt());
            prep.setString(10, user.getPasswordDk());
            prep.setTimestamp (11, new java.sql.Timestamp(user.getRegisterDT().getTime()));
            prep.setString(12, user.getCulture());
            prep.setString(13, user.getGender());
            prep.setString(14, user.getEmailConfirmCode());
            prep.setString(15, user.getPhoneConfirmCode());

            prep.executeUpdate();

        }
        catch( SQLException ex ) {
            logger.log(
                    Level.SEVERE,
                    ex.getMessage() + "--" + sql
            ) ;
            throw new RuntimeException(ex);
        }
    }

    public boolean confirmEmailCode( User user, String code ) {
        if( user == null
                || code == null
                || ! code.equals( user.getEmailConfirmCode() )
        ) {
            return false ;
        }
        String sql = "UPDATE " + dbPrefix + "Users SET emailConfirmCode = NULL WHERE id = ?" ;
        try( PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql) ) {

            prep.setString(1, user.getId().toString());
            prep.executeUpdate();
            return true;
        }
        catch( SQLException ex ) {
            logger.log( Level.SEVERE,ex.getMessage() + "--" + sql ) ;
            return false;
        }
    }


    /**
     * Authentication of user
     * @param login
     * @param password
     * @return UserDTO or null
     */
    public  User authenticate(String login, String password)
    {
        //1. цукаємо користувача по логіну
        //2, вилучаємо сіль та пк
        //3. Генеруємо похідний ключ перевіряємо рівність збереженому пк


        String sql = "SELECT u.* FROM " + dbPrefix + "Users u WHERE u.login = ?";;
        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql))
        {
            prep.setString(1,login);
            ResultSet res = prep.executeQuery();
            if(res.next())//є дані
            {
                User user= new User(res);

              if(  kdfService
                        .getDerivedKey(password, user.getSalt())
                        .equals(user.getPasswordDk()))
              {
                  return  user;
              }

            }//else -до кінця і   return  null;
        }
        catch( SQLException ex ) {
            logger.log(
                    Level.SEVERE,
                    ex.getMessage() + "--" + sql
            ) ;
            throw new RuntimeException(ex);
        }
        return  null;

    }

    public void UpdateLastLoginDT(User user )
    {
        String sql = "UPDATE " + dbPrefix +"Users SET `lastLoginDT` = CURRENT_TIMESTAMP " +
                "WHERE `id` = '" + user.getId() + "'" ;
        try( Statement statement = dbProvider.getConnection().createStatement() ) {
            statement.executeUpdate( sql ) ;
        }
        catch( SQLException ex ) {
            logger.log(
                    Level.SEVERE,
                    ex.getMessage() + "--" + sql
            ) ;
            throw new RuntimeException(ex);
        }
    }
}
