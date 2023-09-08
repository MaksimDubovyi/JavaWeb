package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.db.DbProvider;

import javax.servlet.*;
import java.io.IOException;

/*
Ініціює перше підключення, контролює доступність БД
 */
@Singleton
public class DbFilter implements Filter {
    private FilterConfig filterConfig ;

    private  final DbProvider dbProvider;

    @Inject
    public DbFilter(DbProvider dbProvider) {
        this.dbProvider = dbProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig ;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try{
            dbProvider.getConnection();
            filterChain
                    .doFilter( servletRequest, servletResponse ) ;
        }
        catch (RuntimeException ignored){
            servletRequest.getRequestDispatcher("WEB-INF/no-db.jsp")
                    .forward(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {
        this.filterConfig = null ;
    }
}
