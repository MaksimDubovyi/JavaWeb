package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.HashService;
import step.learning.services.KupinaHashService;

public class ServiceConfig extends AbstractModule {
    @Override
    protected void configure() {
       bind(HashService.class).to(KupinaHashService.class);
    }
}
