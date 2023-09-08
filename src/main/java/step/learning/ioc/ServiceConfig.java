package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.HashService;
import step.learning.services.KupinaHashService;
import step.learning.services.db.DbProvider;
import step.learning.services.db.PlanetDbProvider;

public class ServiceConfig extends AbstractModule {
    @Override
    protected void configure() {

        bind(HashService.class).to(KupinaHashService.class);
        bind(DbProvider.class).to(PlanetDbProvider.class);
    }
}
