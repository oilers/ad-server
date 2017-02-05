package org.oiler.ad.server

import io.dropwizard.Application
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.hibernate.HibernateBundle
import io.dropwizard.hibernate.ScanningHibernateBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.oiler.ad.server.core.AdSizeService
import org.oiler.ad.server.core.AuctionService
import org.oiler.ad.server.core.ProviderService
import org.oiler.ad.server.core.UserService
import org.oiler.ad.server.db.AdSizeDAO
import org.oiler.ad.server.db.AuctionDAO
import org.oiler.ad.server.db.ProviderDAO
import org.oiler.ad.server.db.UserDAO
import org.oiler.ad.server.resources.AdSizeResource
import org.oiler.ad.server.resources.AuctionResource
import org.oiler.ad.server.resources.ProviderResource
import org.oiler.ad.server.resources.UserResource

import javax.ws.rs.client.Client
import java.util.concurrent.ExecutorService

public class AdServerApplication extends Application<AdServerConfiguration> {
    private
    final HibernateBundle<AdServerConfiguration> hibernate = new ScanningHibernateBundle<AdServerConfiguration>("org.oiler.ad.server.entities") {
        @Override
        DataSourceFactory getDataSourceFactory(AdServerConfiguration configuration) {
            return configuration.database
        }
    };

    public static void main(final String[] args) throws Exception {
        new AdServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "adServer"
    }

    @Override
    public void initialize(final Bootstrap<AdServerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate)
    }

    @Override
    public void run(final AdServerConfiguration configuration,
                    final Environment environment) {

        final Client client = new JerseyClientBuilder(environment).using(configuration.jerseyClient)
                .build(getName())
        ExecutorService auctionExecutor = environment.lifecycle().executorService("auction-executor").maxThreads(20).build()
        long clickTimer = System.getProperty("CLICKTIMER").toLong()
        UserDAO userDAO = new UserDAO(hibernate.sessionFactory)
        AdSizeDAO adSizeDAO = new AdSizeDAO(hibernate.sessionFactory)
        ProviderDAO providerDAO = new ProviderDAO(hibernate.sessionFactory)
        AuctionDAO auctionDAO = new AuctionDAO(hibernate.sessionFactory)

        AdSizeService adSizeService = new AdSizeService(adSizeDAO: adSizeDAO)
        UserService userService = new UserService(userDAO: userDAO)
        ProviderService providerService = new ProviderService(providerDAO: providerDAO)
        AuctionService auctionService = new AuctionService(auctionDAO: auctionDAO,
                userService: userService,
                providerService: providerService,
                client: client,
                executorService: auctionExecutor,
                sessionFactory: hibernate.sessionFactory,
                clickTimer: clickTimer * 1000)

        UserResource userResource = new UserResource(userService: userService, adSizeService: adSizeService, providerService: providerService)
        ProviderResource providerResource = new ProviderResource(providerService: providerService, adSizeService: adSizeService, userService: userService)
        AdSizeResource adSizeResource = new AdSizeResource(adSizeService: adSizeService)
        AuctionResource adResource = new AuctionResource(auctionService: auctionService)

        environment.jersey().register(adResource)
        environment.jersey().register(userResource)
        environment.jersey().register(providerResource)
        environment.jersey().register(adSizeResource)
    }

}
