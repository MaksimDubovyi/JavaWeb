package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.fileupload.FileItem;
import step.learning.services.dao.UserDao;
import step.learning.services.dao.WebTokenDao;
import step.learning.services.db.dto.User;
import step.learning.services.db.dto.WebToken;
import step.learning.services.email.EmailService;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;
import step.learning.services.kdf.KdfService;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SignupServlet extends HttpServlet {

    private final String uploadPath;
    private  final FormParseService formParseService;
    private final UserDao userDao;
    private final WebTokenDao webTokenDao;
   private final Logger logger;
    private final KdfService kdfService;
    private final EmailService emailService;
    @Inject
    public SignupServlet(@Named("UploadDir") String uploadDir, FormParseService formParseService, UserDao userDao, WebTokenDao webTokenDao, Logger logger, KdfService kdfService, EmailService emailService) {
        this.uploadPath = uploadDir;

        this.formParseService = formParseService;
        this.userDao = userDao;
        this.webTokenDao = webTokenDao;
        this.logger = logger;
        this.kdfService = kdfService;
        this.emailService = emailService;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//метод викликається до того як відбувається розподіл за doXxxx методами,
        //тут є можливість вплинути на цей розподіл
        switch ((req.getMethod().toUpperCase()))
        {

            case "PATCH": this.doPatch(req,resp);
                break;
            default:
                super.service(req,resp);//розподіл за замовчуванням
        }
    }


    protected void doPatch( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if(code==null)
        {
            resp.setStatus(400);
            resp.getWriter().print("Missing required parameter: code");
            return;
        }
        String authHeader = req.getHeader("Authorization");
        if(authHeader==null)
        {
            resp.setStatus(401);
            resp.getWriter().print("Unauthorized");
            return;
        }
        User user = webTokenDao.getSubject(authHeader);
        if(user==null)
        {
            resp.setStatus(403);
            resp.getWriter().print("Forbidden: invalid or expired token");
            return;
        }
        // перевіряємо збіг коду у БД та коду у запиті
        if(userDao.confirmEmailCode(user,code))
        {
            resp.setStatus(202);
            resp.getWriter().print("Code accepted");

        }
        else {
            resp.setStatus(203);
            resp.getWriter().print("Code rejected");

        }
    }

    protected void doGet( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageName", "signup" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward( req, resp ) ;
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Автентифікація - перевірка логіну/паролю
        ResponseData responseData ;
        try {
        /* // При передачі URL-параметрів
        String login = req.getParameter( "auth-login" ) ;
        String password = req.getParameter( "auth-password" ) ;
         */
        /*  // При передачі form-data (див. зміни у MixedFormParseService)
        FormParseResult parseResult = formParseService.parse( req ) ;
        String login = parseResult.getFields().get("auth-login");
        String password = parseResult.getFields().get("auth-password");
         */
            // При передачі JSON
            JsonObject json = JsonParser.parseReader(req.getReader()).getAsJsonObject();
            String login = json.get("auth-login").getAsString() ;
            String password = json.get("auth-password").getAsString() ;

            User user = userDao.authenticate( login, password ) ;
            if( user != null ) {
                //Оновлюємо дату останнього входу (lastLoginDT)
                  userDao.UpdateLastLoginDT(user);

                //генеруємо токен та повертаємо його у відповідь
                WebToken webToken = webTokenDao.create(user);
                responseData= new ResponseData( 200,webToken.toBase64() ) ;
            }
            else {
                responseData= new ResponseData( 401, "Unauthorized" ) ;
            }
        }
        catch( Exception ex ) {
            logger.log( Level.SEVERE, ex.getMessage() ) ;
            responseData = new ResponseData(500, "There was an error. Look at server's logs");
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        resp.getWriter().print( gson.toJson( responseData ) ) ;
    }
// upload settings

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignupFormData formData ;
        ResponseData responseData;
        try {
            formData = new SignupFormData( req ) ;
            User user = formData.toUserDto();
            userDao.add(user);
            //send confirm codes
            Message message = emailService.prepareMessage() ;
            message.setRecipients(  Message.RecipientType.TO,InternetAddress.parse( user.getEmail()) ) ;
            message.setContent(
                    String.format(  "<b>Вітаємо</b> з реєстрацією на <a href='https://javawebaa.azurewebsites.net'> сайті JavaWeb </a>! </br>"+
                            "Для підтвердження пошти використайте код <b style='font-size:large; color=green'>%s</b>",
                            user.getEmailConfirmCode()),
                            "text/html; charset=UTF-8" );
            emailService.send( message ) ;
            responseData= new ResponseData(200,"Ok");

        }
        catch( Exception ex ) {
            logger.log( Level.SEVERE, ex.getMessage() ) ;
            responseData = new ResponseData(500, "There was an error. Look at server's logs");
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        resp.getWriter().print(
                gson.toJson( responseData )
        ) ;
    }

    class ResponseData
    {
        public ResponseData() {

        }
        public ResponseData(int statusCode, String massage) {
            this.statusCode = statusCode;
            this.message = massage;
        }

        int statusCode;
    String message;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getMassage() {
            return message;
        }

        public void setMassage(String massage) {
            this.message = massage;
        }
    }



    class SignupFormData {
        private String name;
        private String lastname;
        private String email;
        private String phone;
        private Date birthdate;
        private String login;
        private String password;
        private String culture;
        private String gender;
        private String avatar;
        private SimpleDateFormat dateParser =
                new SimpleDateFormat("yyyy-MM-dd") ;

        public SignupFormData( HttpServletRequest req ) {
            FormParseResult parseResult = formParseService.parse( req ) ;
            Map<String, String> fields = parseResult.getFields() ;
            setName( fields.get( "reg-name" ) ) ;
            setLastname( fields.get( "reg-lastname" ) ) ;
            setEmail( fields.get( "reg-email" ) ) ;
            setPhone( fields.get( "reg-phone" ) ) ;
            setLogin( fields.get( "reg-login" ) ) ;
            setPassword( fields.get( "reg-password" ) ) ;
            setCulture( fields.get( "reg-culture" ) ) ;
            setGender( fields.get( "reg-gender" ) ) ;
            setBirthdate( fields.get( "reg-birthdate" ) ) ;

            Map<String,FileItem> files=parseResult.getFiles();
            if(files.containsKey("reg-avatar"))
            {
                setAvatar(files.get("reg-avatar"));
            }
            else
            {
                setAvatar((String) null);
            }
        }

        public User toUserDto() {
            User user = new User() ;
            user.setAvatar( this.getAvatar() ) ;
            user.setFirstName( this.getName() ) ;
            user.setLastName( this.getLastname() ) ;
            user.setLogin( this.getLogin() ) ;
            user.setGender( this.getGender() ) ;
            user.setCulture( this.getCulture() ) ;
            user.setBirthdate( this.getBirthdate() ) ;
            user.setPhone( this.getPhone() ) ;
            if(user.getPhone()!=null)
            {//генеруємо еод підтвердження
                String phoneCode= UUID.randomUUID().toString().substring(0, 6);
                //зберігаємо в обєкті
                user.setPhoneConfirmCode(phoneCode);
                //та надсилаємо (TODO)
            }
            user.setEmail( this.getEmail() ) ;
            if(user.getEmail()!=null)
            {//генеруємо еод підтвердження
                String emailCode= UUID.randomUUID().toString().substring(0, 6);
                //зберігаємо в обєкті
                user.setEmailConfirmCode(emailCode);
                //та надсилаємо (TODO)
            }
            user.setId( UUID.randomUUID() ) ;

            user.setDeleteDT( null ) ;
            user.setBanDT( null ) ;
            user.setRegisterDT( new Date() ) ;
            user.setLastLoginDT( null ) ;

            user.setSalt( user.getId().toString().substring(0, 8) ) ;
            user.setPasswordDk( kdfService.getDerivedKey( this.getPassword(), user.getSalt() ) ) ;
            user.setRoleId(null);
            return user ;
        }
        public String getAvatar() {
            return avatar;
        }
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public void setAvatar(FileItem avatar) {

            String extension="."+org.apache.commons.io.FilenameUtils.getExtension(new File(avatar.getName()).getName());    //відокремлюємо розширення файлу
            extension=extension.toLowerCase();

            if(CheckExtension(extension))  // перевіряємо на допустимі розширення
            {
                 String  fileName = UUID.randomUUID().toString().substring(0, 8)+extension; // - генеруємо випадкове ім'я
                 String newFileName=CheckIfNameExists(fileName,extension);      // - перевіряємо, чи випадково не потрапили у наявне ім'я, перегенеруємо
                 String filePath = getServletContext().getRealPath("") + uploadPath+ File.separator +newFileName;

                setAvatar(filePath);

                   File storeFile = new File(filePath);
                   try {
                       avatar.write(storeFile);
                   } catch (Exception e) {
                       throw new RuntimeException("Помилка при збереженні файлу: " + e.getMessage(), e);
                   }
            }
            else
            {
                setAvatar((String) null);
            }
        }

        // перевіряємо на допустимі розширення
        public boolean CheckExtension(String extension)
        {
            if(extension.equals(".jpg")||extension.equals(".jpeg")||extension.equals(".png")||extension.equals(".gif")||extension.equals(".bmp")||extension.equals(".svg"))
            {
                return true;
            }
            return false;
        }
        public String CheckIfNameExists(String name,String extension)
        {
            String fileName=name+extension;
            String uploadPathq = getServletContext().getRealPath("") + File.separator + uploadPath; //формування шляху до папки upload
            File directory = new File(uploadPathq);

            // чи папка існує і є директорією
            if (directory.exists() && directory.isDirectory())
            {
                while (true)
            {
                File[] files = directory.listFiles();
                int x=0;
                //всі файли в папці
                for (File file : files) {
                    if (file.isFile()) {

                        if(fileName.equals(file.getName()))
                        {
                            x++;
                            fileName = UUID.randomUUID().toString().substring(0, 8)+extension; // - генеруємо випадкове ім'я
                        }
                    }
                  }
                if(x==0)
                    return fileName;
                else x=0;

                }
            }
            else
            {
                System.err.println("Папку не знайдено або це не є директорією.");
            }
            return fileName;
         }



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public void setBirthdate(String birthdate) {
            if( birthdate != null && ! birthdate.isEmpty() ) {
                try {
                    setBirthdate( dateParser.parse( birthdate ) ) ;
                }
                catch( ParseException ex ) {
                    throw new RuntimeException( ex ) ;
                }
            }
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCulture() {
            return culture;
        }

        public void setCulture(String culture) {
            this.culture = culture;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }


    }

}
