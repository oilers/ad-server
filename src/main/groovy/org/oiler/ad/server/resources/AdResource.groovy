package org.oiler.ad.server.resources

import org.oiler.ad.server.entities.AdResponse
import org.oiler.ad.server.entities.AdTransaction

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by Kodi on 2/4/2017.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
class AdResource {

    @GET
    @Path("ad")
    public AdResponse getAd(
            @QueryParam("width") Long width,
            @QueryParam("height") Long height, @QueryParam("userid") Long userId, @QueryParam("url") String url) {
        if (width && height && userId && url) {
            return new AdResponse(tid: UUID.randomUUID().toString(), html: "<p>BUY ME</p>")
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "Must specify width, height, userid, and url"]).build())
        }
    }

    @GET
    @Path("click")
    public Response activateClick(@QueryParam("tid") String transacionId, @QueryParam("userid") Long userId) {
        if (transacionId && userId) {
            return Response.ok([success: true]).build()
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "Must specify tid and userid"]).build())
        }
    }


    @GET
    @Path("history")
    public Collection<AdTransaction> getHistory() {
        return []

    }
}
