package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.fileupload.FileItem;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;

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

@Singleton
public class SignupServlet extends HttpServlet {

    private final String uploadPath;
    private  final FormParseService formParseService;

    @Inject
    public SignupServlet(@Named("UploadDir") String uploadDir,FormParseService formParseService) {
        this.uploadPath = uploadDir;

        this.formParseService = formParseService;
    }

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageName", "signup" ) ;
        req.getRequestDispatcher( "WEB-INF/_layout.jsp" ).forward( req, resp ) ;
    }


    // upload settings

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignupFormData formData ;
        try {
            formData = new SignupFormData( req ) ;

        }
        catch( Exception ex ) {
            resp.getWriter().print(
                    "There was an error: " + ex.getMessage()
            ) ;
            formData = null ;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        resp.getWriter().print(
                gson.toJson( formData )
        ) ;
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
                 String fileName =  org.apache.commons.io.FilenameUtils.getBaseName(new File(avatar.getName()).getName());  //відокремлюємо ім'я файлу
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
