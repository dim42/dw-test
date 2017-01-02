package pack1.dw;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DWApplication extends Application<DWConfiguration> {
    public static void main(String[] args) throws Exception {
        new DWApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<DWConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(DWConfiguration configuration, Environment environment) {
        String template = configuration.getTemplate();
        DWResource resource = new DWResource(template, configuration.getDefaultName());
        HealthCheck healthCheck = new DWHealthCheck(template);
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }
}
