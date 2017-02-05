package org.oiler.ad.server.resources

import org.oiler.ad.server.api.AuctionModel
import org.oiler.ad.server.api.AuctionParamaters
import org.oiler.ad.server.core.AuctionService
import org.oiler.ad.server.api.AuctionResult
import org.oiler.ad.server.api.ClickResult

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
class AdResource {
    AuctionService auctionService
    long staleMilliseconds

    @GET
    @Path("ad")
    public AuctionResult getAd(
            @QueryParam("width") Integer width,
            @QueryParam("height") Integer height, @QueryParam("userid") Integer userId, @QueryParam("url") URI url,
            @HeaderParam("user-agent") String userAgent, @Context HttpServletRequest request) {
        if (width && height && userId && url) {
            String ip = request.remoteAddr
            String domain = url.host
            String transactionId = UUID.randomUUID().toString()
            AuctionResult auctionResult = auctionService.performAuction(new AuctionParamaters(
                    ip: ip,
                    domain: domain,
                    userAgent: userAgent,
                    transactionId: transactionId,
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
    public Response activateClick(@QueryParam("tid") String transacionId, @QueryParam("userid") Long userId) {
        if (transacionId && userId) {
            def auction = auctionService.getAuction(transacionId)
            if (auction.performed.time + staleMilliseconds < System.currentTimeMillis()) {
                auction.clickResult = ClickResult.CLICK
            } else {
                auction.clickResult = ClickResult.STALE
            }
            auctionService.saveAuction(auction)
            return Response.ok([success: true]).build()
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "Must specify tid and userid"]).build())
        }
    }


    @GET
    @Path("history")
    public Collection<AuctionModel> getHistory() {
        return auctionService.getAuctions().collect { it.toModel() }

    }
}
