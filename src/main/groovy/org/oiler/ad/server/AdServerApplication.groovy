package org.oiler.ad.server;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment
import org.oiler.ad.server.resources.AdResource;

public class AdServerApplication extends Application<AdServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new AdServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "adServer";
    }

    @Override
    public void initialize(final Bootstrap<AdServerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final AdServerConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new AdResource())
    }

}
