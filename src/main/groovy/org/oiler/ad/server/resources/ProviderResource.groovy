package org.oiler.ad.server.resources

import io.dropwizard.hibernate.UnitOfWork
import org.oiler.ad.server.entities.Provider
import org.oiler.ad.server.entities.User
import org.oiler.ad.server.core.AdSizeService
import org.oiler.ad.server.core.ProviderService
import org.oiler.ad.server.core.UserService

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by Kodi on 2/4/2017.
 */
@Path("provider")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProviderResource {
    ProviderService providerService
    UserService userService
    AdSizeService adSizeService

    @GET
    @Path("{providerId}")
    @UnitOfWork
    public Provider getProvider(@PathParam("providerId") int providerId) {
        return providerService.getProvider(providerId)
    }

    @POST
    @Path("{providerId}")
    @UnitOfWork
    public Provider updateProvider(@PathParam("providerId") int providerId, Provider provider) {
        if (providerId == provider.providerId) {
            return providerService.saveProvider(provider)
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "providerIds do not match"]).build())
        }
    }


    @POST
    @Path("{providerId}/adSize/{adSizeId}")
    @UnitOfWork
    public User associateAdSize(@PathParam("providerId") int providerId, @PathParam("adSizeId") int adSizeId) {
        def provider = providerService.getProvider(providerId)
        def adSize = adSizeService.getAdSize(adSizeId)
        return providerService.associateAdSize(provider, adSize)
    }

    @POST
    @Path("{providerId}/provider/{userId}")
    @UnitOfWork
    public Provider associateProvider(@PathParam("providerId") int providerId, @PathParam("userId") int userId) {
        def user = userService.getUser(userId)
        def provider = providerService.getProvider(providerId)
        return providerService.associateUser(provider, user)
    }

    @PUT
    @UnitOfWork
    public Provider createProvider(Provider provider) {
        return providerService.saveProvider(provider)
    }

}
