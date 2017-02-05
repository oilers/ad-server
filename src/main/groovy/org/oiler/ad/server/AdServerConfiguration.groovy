package org.oiler.ad.server

import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientConfiguration
import io.dropwizard.db.DataSourceFactory

import javax.validation.Valid
import javax.validation.constraints.NotNull;

public class AdServerConfiguration extends Configuration {
    @Valid
    @NotNull
    DataSourceFactory database = new DataSourceFactory()

    @Valid
    @NotNull
    JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration()

    @NotNull
    long staleTimeMillis
}
