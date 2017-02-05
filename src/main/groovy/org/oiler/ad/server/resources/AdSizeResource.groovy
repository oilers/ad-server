package org.oiler.ad.server.resources

import io.dropwizard.hibernate.UnitOfWork
import org.oiler.ad.server.api.AdSizeModel
import org.oiler.ad.server.core.AdSizeService
import org.oiler.ad.server.entities.AdSize

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by Kodi on 2/4/2017.
 */
@Path("adSize")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AdSizeResource {
    AdSizeService adSizeService

    @GET
    @Path("{adSizeId}")
    @UnitOfWork
    public AdSizeModel getAdSize(@PathParam("adSizeId") int adSizeId) {
        return adSizeService.getAdSize(adSizeId).toModel()
    }

    @GET
    @UnitOfWork
    public Collection<AdSizeModel> getAdSizes() {
        return adSizeService.adSizes.collect { it.toModel() }
    }

    @POST
    @Path("{adSizeId}")
    @UnitOfWork
    public AdSizeModel updateAdSize(@PathParam("adSizeId") int adSizeId, AdSize adSize) {
        if (adSizeId == adSize.adSizeId) {
            return adSizeService.saveAdSize(adSize).toModel()
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "adSizeIds do not match"]).build())
        }
    }

    @PUT
    @UnitOfWork
    public AdSizeModel createAdSize(AdSize adSize) {
        return adSizeService.saveAdSize(adSize).toModel()
    }

}
