package org.oiler.ad.server.core

import org.oiler.ad.server.api.AuctionParamaters
import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.api.BidRequest
import org.oiler.ad.server.api.ProviderModel
import org.oiler.ad.server.db.AuctionDAO
import org.oiler.ad.server.entities.Auction
import org.oiler.ad.server.api.AuctionResult

import javax.ws.rs.client.Client
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by Kodi on 2/4/2017.
 */
class AuctionService {
    public static final BidModel EMPTY_BID = new BidModel(adhtml: "<span></span>", bidprice: 0.0)
    AuctionDAO auctionDAO
    Client client
    ProviderService providerService
    ExecutorService executorService
    UserService userService

    Auction saveAuction(Auction auction) {
        return auctionDAO.save(auction)
    }

    Auction getAuction(String id) {
        return auctionDAO.findById(id)
    }

    Collection<Auction> getAuctions() {
        return auctionDAO.findAll()
    }

    AuctionResult performAuction(AuctionParamaters auctionParamaters) {
        def userId = auctionParamaters.userId
        def transactionId = auctionParamaters.transactionId
        def width = auctionParamaters.width
        def height = auctionParamaters.height
        Collection<ProviderModel> providers = providerService.getProviderBySizeAndUser(width, height, userId)
        def futures = executorService.invokeAll(providers.collect {
            new BidCollecter(
                    target: client.target(it.url),
                    bidRequest: new BidRequest(width: width, height: height,
                            useragent: auctionParamaters.userAgent, domain: auctionParamaters.domain, userip: auctionParamaters.ip),
                    providerId: it.providerId
            )
        }, 150, TimeUnit.MILLISECONDS)

        Collection<BidModel> bids = new ArrayList<>(futures.size())
        BidModel maxBid = EMPTY_BID
        futures.each { future ->
            if (!future.cancelled) {
                BidModel bid = future.get()
                bids << bid
                if (bid.bidprice > maxBid.bidprice) {
                    maxBid = bid
                }
            }
        }
        recordAuction(bids, maxBid, transactionId, userId)
        return new AuctionResult(tid: transactionId, html: maxBid.adhtml)
    }

    def recordAuction(Collection<BidModel> bids, BidModel winningBid, String transactionId, int userId) {
        executorService.submit(new AuctionRecorder(bids: bids,
                winningBid: winningBid,
                transactionId: transactionId,
                userId: userId,
                providerService: providerService,
                userService: userService,
                auctionService: this
        ))
    }
}