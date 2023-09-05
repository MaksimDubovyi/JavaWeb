package step.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContextEvent;

public class IocListener extends GuiceServletContextListener {
//    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        super.contextInitialized(servletContextEvent);
//    }

    @Override
    protected Injector getInjector() {
       return Guice.createInjector(
               new FilterConfig(),
               new ServletConfig(),
               new LoggerConfig(),
               new ServiceConfig()
       );
    }
}
/*
Інверсія управління у веб-проєктах (додавання Guice до веб-проєкту)

1. Залежномті (пакети): pom.xml
    -- https://mvnrepository.com/artifact/com.google.inject/guice
    (перевірено на версії 6,0,0)
    -- https://mvnrepository.com/artifact/com.google.inject.extensions/guice-servlet
    (перевірено на версії 6,0,0)
2. Налаштування web.xml
    - прибираємо з web.xml усі налаштування сервлетів та фільтрів, (вони будуть перенесені у конфігурацію Guice)
    -  додаємо фільтр Guice та наш слухач події створення контексту (старту веб роботи)
    (певний аналог точки main) --- клас з цього файлу
3. Створюємо конфіг- класи (один або декілька) зазнеачаємо фільтри та сервлети.
    !! для всіх класів фільтрів та сервлетів
* */