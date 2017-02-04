package org.oiler.ad.server

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

import javax.validation.Valid
import javax.validation.constraints.NotNull;

public class AdServerConfiguration extends Configuration {
    @Valid
    @NotNull
    DataSourceFactory database = new DataSourceFactory()
}
