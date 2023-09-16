package step.learning.services.formparse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for multipart and urlencoded form
 * **/
@Singleton
public class MixedFormParseService implements FormParseService {

    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 10;  // 10MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    private ServletFileUpload fileUpload;

    @Inject
    public MixedFormParseService() {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        fileUpload = new ServletFileUpload(factory);
        fileUpload.setFileSizeMax(MAX_FILE_SIZE);
        fileUpload.setSizeMax(MAX_REQUEST_SIZE);


    }

    @Override
    public FormParseResult parse(HttpServletRequest request) {
//        String uploadPath = request.getServletContext().getRealPath("")
//                + File.separator + uploadDir;
//        File uploadDir = new File(uploadPath);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }
        // готуємо колекції для результату
        Map<String, String>  fields = new HashMap<>() ;
        Map<String, FileItem> files = new HashMap<>() ;

        // розділяємо роботу в залежності від типу запиту (multipart/urlencoded)
        if( ServletFileUpload.isMultipartContent( request ) ) {  // multipart
            try {
                // розбираємо запит використовуючи створений у конструкторі парсер
                for( FileItem fileItem : fileUpload.parseRequest( request ) ) {
                    // і перевіряємо кожну частину (part of multipart)
                    if( fileItem.isFormField() ) {     // якщо це поле форми -
                        fields.put(                   // то додаємо його до колекції  fields
                                fileItem.getFieldName(),
                                fileItem.getString("UTF-8")
                        ) ;
                    }
                    else {  // інакше - це файлова частина
                        files.put(   // додаємо відомості про неї до колекції files
                                fileItem.getFieldName(),
                                fileItem
                        ) ;
                    }
                }
            }
            catch( FileUploadException | UnsupportedEncodingException ex ) {
                throw new RuntimeException( ex ) ;
            }
        }
        else {  // urlencoded
            // користуємось стандартними засобами ServletAPI
            Enumeration<String> parameterNames = request.getParameterNames() ;
            // перебираємо імена всіх параметрів запиту та перекладаємо у колекцію
            while( parameterNames.hasMoreElements() ) {
                String name = parameterNames.nextElement() ;
                fields.put( name, request.getParameter( name ) ) ;
            }
        }
        return new FormParseResult() {
            @Override public Map<String, String> getFields() { return fields ; }
            @Override public Map<String, FileItem> getFiles() { return files ; }
        };
    }
}
