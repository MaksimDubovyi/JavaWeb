package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.services.HashService;
import step.learning.servlets.*;

/*
Конфігурація Servlet для Cuice
 */
public class ServletConfig  extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/").with(HomeServlet.class);
        serve("/jsp").with(JspServlet.class);
        serve("/about").with(AboutServlet.class);
        serve("/url").with(UrlServlet.class);
        serve("/security").with(SecurityServlet.class);
        serve("/hash").with(HashServlet.class);
        serve("/install").with(InstallServlet.class);
    }
}
