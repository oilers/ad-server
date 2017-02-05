package org.oiler.ad.server.core

import groovy.util.logging.Slf4j
import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.api.BidRequest

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import java.util.concurrent.Callable

/**
 * Created by Kodi on 2/4/2017.
 */
@Slf4j
class BidCollecter implements Callable<BidModel> {
    WebTarget target
    BidRequest bidRequest
    int providerId

    @Override
    BidModel call() throws Exception {
        try {
            def bid = target.request().post(Entity.json(bidRequest), BidModel)
            bid.providerId = providerId
            return bid
        } catch (Exception ex) {
            log.warn("Unable to ask provider ${providerId} for a bid", ex)
            return AuctionService.EMPTY_BID
        }
    }
}
