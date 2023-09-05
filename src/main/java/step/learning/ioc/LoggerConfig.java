package step.learning.ioc;

import com.google.inject.AbstractModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LoggerConfig extends AbstractModule {
    @Override
    protected void configure() {
        try(InputStream propertiesStream=
                this.getClass()//звертаємось до типу
                        .getClassLoader()//дістаємо завантажувач типів
                        .getResourceAsStream("logging.properties")///звертаємось до ресурсу
            //папка resource є частиною проєкту  .getResourceAsStream звертається до неї

                ){
            LogManager logManager=LogManager.getLogManager();
            logManager.reset();
            logManager.readConfiguration(propertiesStream);
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
/*
Налаштування логеру (для Guice)
-у папку resources створюємо/копіюємо файл logging.properties (він є частиною JDK/JRE
зразок можна знайти у пакетах встановлення Java або в Інтернеті)
- у класі конфігурацій передаємо вміст цього файлу до нгалаштувань логера
- додаємо цей клас до кофігурацій Guice
* */
