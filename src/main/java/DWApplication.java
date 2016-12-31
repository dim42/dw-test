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
        // nothing to do yet
    }
}
