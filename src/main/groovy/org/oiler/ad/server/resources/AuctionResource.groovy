package org.oiler.ad.server.resources

import io.dropwizard.hibernate.UnitOfWork
import org.oiler.ad.server.api.AuctionModel
import org.oiler.ad.server.api.AuctionParamaters
import org.oiler.ad.server.api.AuctionResult
import org.oiler.ad.server.core.AuctionService

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by Kodi on 2/4/2017.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
class AuctionResource {
    AuctionService auctionService

    @GET
    @Path("ad")
    @UnitOfWork
    public AuctionResult getAd(
            @QueryParam("width") Integer width,
            @QueryParam("height") Integer height, @QueryParam("userid") Integer userId, @QueryParam("url") URI url,
            @HeaderParam("User-Agent") String userAgent, @Context HttpServletRequest request) {
        if (width && height && userId && url) {
            String ip = request.remoteAddr
            String domain = url.host
            AuctionResult auctionResult = auctionService.performAuction(new AuctionParamaters(
                    ip: ip,
                    domain: domain,
                    userAgent: userAgent,
                    width: width,
                    height: height,
                    userId: userId))
            return auctionResult
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "Must specify width, height, userid, and url"]).build())
        }
    }

    @GET
    @Path("click")
    @UnitOfWork
    public Response activateClick(@QueryParam("tid") String transactionId, @QueryParam("userid") Integer userId) {
        if (transactionId && userId) {
            def clickResult = auctionService.registerClick(transactionId, userId)
            return Response.ok([success: true, clickResult: clickResult]).build()
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "Must specify tid and userid"]).build())
        }
    }


    @GET
    @Path("history")
    @UnitOfWork
    public Collection<AuctionModel> getHistory() {
        return auctionService.getAuctions().collect { it.toModel() }

    }
}
