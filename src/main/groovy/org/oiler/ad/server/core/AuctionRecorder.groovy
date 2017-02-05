package org.oiler.ad.server.core

import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.entities.Auction
import org.oiler.ad.server.entities.Bid


/**
 * Created by Kodi on 2/4/2017.
 */
class AuctionRecorder implements Runnable {
    Collection<BidModel> bids
    BidModel winningBid
    String transactionId
    int userId
    ProviderService providerService
    UserService userService
    AuctionService auctionService

    @Override
    void run() {
        Auction auction = new Auction()
        auction.winningPrice = winningBid.bidprice
        auction.winningProvider = providerService.getProvider(winningBid.providerId)
        auction.transactionId = transactionId
        auction.user = userService.getUser(userId)
        auction.bids = bids.collect { Bid.fromModel(it, providerService) }
        auctionService.saveAuction(auction)
    }
}
