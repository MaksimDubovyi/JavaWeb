package step.learning.services.db.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class WebToken {   // https://en.wikipedia.org/wiki/JSON_Web_Token
    private UUID id;
    private UUID sub;  // Subject (userId)
    private Date exp;  // Expiration Time  (Термін придатності)
    private Date iat;  // Issued at  (Виданий в)

    private static final SimpleDateFormat dateParser =
            new SimpleDateFormat("MMM dd, yyyy h:mm:ss a", Locale.ENGLISH);


    public WebToken() {}
    public WebToken( String base64String ) throws ParseException {
        JsonObject json = JsonParser.parseString(
                new String(
                        Base64.getDecoder().decode(base64String),
                        StandardCharsets.UTF_8
                )
        ).getAsJsonObject() ;
        setId( UUID.fromString( json.get( "id" ).getAsString() ) ) ;
        setSub( UUID.fromString( json.get( "sub" ).getAsString() ) ) ;
        setExp( dateParser.parse(json.get( "exp" ).getAsString() ) ) ;
        setIat(dateParser.parse(json.get( "iat" ).getAsString() ) ) ;

    }
    public WebToken( ResultSet resultSet ) throws SQLException {
        setId( UUID.fromString( resultSet.getString("id") ) ) ;
        setSub( UUID.fromString( resultSet.getString("sub") ) ) ;
        setExp( resultSet.getDate( "exp" ) ) ;
        setIat( resultSet.getDate( "iat" ) ) ;
    }
    public String toBase64()
    {
        return  Base64.getUrlEncoder().encodeToString(
                new Gson().toJson(this).getBytes()
           );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSub() {
        return sub;
    }

    public void setSub(UUID sub) {
        this.sub = sub;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }
}