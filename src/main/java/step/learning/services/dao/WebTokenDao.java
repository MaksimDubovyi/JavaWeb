package step.learning.services.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import step.learning.services.db.DbProvider;
import step.learning.services.db.dto.User;
import step.learning.services.db.dto.WebToken;

import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebTokenDao {
    private final DbProvider dbProvider ;
    private final Logger logger;
    private  final String dbPrefix;

    @Inject
    public WebTokenDao(DbProvider dbProvider, Logger logger, @Named("DbPrefix")String dbPrefix) {
        this.dbProvider = dbProvider;
        this.logger = logger;
        this.dbPrefix = dbPrefix;
    }

    public WebToken get( User user ) {
        if( user == null ) {
            return null ;
        }
        String sql = "SELECT w.`id`, w.`sub`, w.`exp`, w.`iat` FROM " + dbPrefix + "WebTokens w " +
                "WHERE w.`sub` = ? AND w.`exp` > CURRENT_TIMESTAMP" ;
        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, user.getId().toString() ) ;
            ResultSet res = prep.executeQuery() ;
            if( res.next() ) {
                return new WebToken(res);
            }
        }
        catch( SQLException ex ) {
            logger.log(
                    Level.SEVERE,
                    ex.getMessage() + "--" + sql
            ) ;
        }
        return null ;
    }

    public WebToken create( User user ) {
        // upd: Перевірити чи є у даного користувача активний токен, якщо є - подовжити
        //      його та повернути (не генерувати новий)
        if( user == null ) {
            return null ;
        }
        // перевіряємо - шукаємо активний токен
        WebToken activeToken ;
        try {
            activeToken = this.get( user ) ;
        }
        catch( RuntimeException ignored ) {
            activeToken = null ;
        }
        String sql ;
        if( activeToken == null ) {  // генеруємо новий токен
            sql = "INSERT INTO " + dbPrefix + "WebTokens (`id`,`sub`,`exp`,`iat`) VALUES (?,?,?,?) " ;
            try(PreparedStatement prep = dbProvider.getConnection().prepareStatement( sql ) ) {
                WebToken webToken = new WebToken() ;
                webToken.setId( UUID.randomUUID() ) ;
                webToken.setSub( user.getId() ) ;
                webToken.setIat( new Date() ) ;
                webToken.setExp( new Date(
                        webToken.getIat().getTime() + 60*60*24*1000
                ) ) ;

                prep.setString( 1, webToken.getId().toString() ) ;
                prep.setString( 2, webToken.getSub().toString() ) ;
                prep.setTimestamp( 3, new Timestamp( webToken.getExp().getTime() ) ) ;
                prep.setTimestamp( 4, new Timestamp( webToken.getIat().getTime() ) ) ;

                prep.executeUpdate() ;
                return webToken ;
            }
            catch( SQLException ex ) {
                logger.log(
                        Level.SEVERE,
                        ex.getMessage() + "--" + sql
                ) ;
                throw new RuntimeException(ex);
            }
        }
        else {  // токен існує - оновлюємо його термін
            sql = "UPDATE " + dbPrefix +"WebTokens SET `exp` = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 DAY) " +
                    "WHERE `id` = '" + activeToken.getId() + "'" ;
            try( Statement statement = dbProvider.getConnection().createStatement() ) {
                statement.executeUpdate( sql ) ;
                activeToken.setIat(new Date(activeToken.getIat().getTime()));
                activeToken.setExp( new Date( new Date().getTime() + 24*60*60*1000 ) ) ;
                return activeToken ;
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

    public User getSubject( WebToken token ) {
        if(token==null ||
                token.getId()==null||
                token.getSub()==null||
                token.getExp()==null||
                token.getExp().before(new Date()) )
        {
            return null ;
        }

        String sql = "SELECT u. * "+
                "FROM "+ dbPrefix +"Users u "+
                "JOIN " + dbPrefix +"WebTokens w ON u.`id` = w.`sub` "+
                "WHERE w.`id` = ? AND w.`exp` > CURRENT_TIMESTAMP ";
            try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql))
            {
                prep.setString(1,token.getId().toString());
                ResultSet res =prep.executeQuery();
                if(res.next())
                {
                    return new User(res);
                }
            }
            catch (SQLException ex)
            {
                logger.log(Level.SEVERE,ex.getMessage()+sql);

            }
        return null ;
    }


    public void install() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + dbPrefix +"WebTokens (" +
                "id    CHAR(36)   PRIMARY KEY," +
                "sub   CHAR(36)   COMMENT 'User Id'," +
                "exp   DATETIME   NOT NULL," +
                "iat   DATETIME   DEFAULT CURRENT_TIMESTAMP" +
                ") Engine = InnoDB  DEFAULT CHARSET = utf8" ;
        try(Statement statement=dbProvider.getConnection().createStatement())
        {
            statement.executeUpdate(createTableSQL);

        }
        catch (SQLException ex)
        {
            logger.log(Level.SEVERE,ex.getMessage()+"--"+"createTableSQL"+"--"+createTableSQL);
            throw new RuntimeException(ex);
        }
    }
}
