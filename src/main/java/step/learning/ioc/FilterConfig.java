package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.filters.DbFilter;
import step.learning.filters.RegistrationFilter;

/*
Конфігурація фільтрів для Cuice
 */
public class FilterConfig extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(CharsetFilter.class);
        filter("/*").through(RegistrationFilter.class);
        filter("/*").through(DbFilter.class);
    }
}
