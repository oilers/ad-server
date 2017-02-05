package org.oiler.ad.server.core

import org.hibernate.SessionFactory
import org.oiler.ad.server.api.*
import org.oiler.ad.server.db.AuctionDAO
import org.oiler.ad.server.entities.Auction

import javax.ws.rs.client.Client
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by Kodi on 2/4/2017.
 */
class AuctionService {
    public static final BidModel EMPTY_BID = new BidModel(adhtml: "<span></span>", bidprice: 0.0, providerId: -1)
    AuctionDAO auctionDAO
    Client client
    ProviderService providerService
    ExecutorService executorService
    UserService userService
    SessionFactory sessionFactory
    Long clickTimer

    Auction saveAuction(Auction auction) {
        return auctionDAO.save(auction)
    }

    Auction getAuction(String id) {
        return auctionDAO.findById(id)
    }

    Collection<Auction> getAuctions() {
        return auctionDAO.findAll()
    }

    /**
     * Performs an auction for an ad request.
     * We look up providers that are connected to both the user and ad size.
     * Each provider has 150ms to respond to our request for a bid, responses are ignored after this timeout.
     * If no valid bids are recieved, a default empty bid is the "winner"
     * Note: Users are also tied to ad sizes and this restricts which providers can be contacted
     * @param auctionParameters
     * @return The html and the price of the winning bid
     */
    AuctionResult performAuction(AuctionParamaters auctionParameters) {
        def userId = auctionParameters.userId
        def width = auctionParameters.width
        def height = auctionParameters.height
        Collection<ProviderModel> providers = providerService.getProviderBySizeAndUser(width, height, userId)
        def futures = executorService.invokeAll(providers.collect {
            new BidCollecter(
                    target: client.target(it.url),
                    bidRequest: new BidRequest(width: width, height: height,
                            useragent: auctionParameters.userAgent, domain: auctionParameters.domain, userip: auctionParameters.ip),
                    providerId: it.providerId
            )
        }, 150, TimeUnit.MILLISECONDS)

        String transactionId = UUID.randomUUID().toString()
        Collection<BidModel> bids = new ArrayList<>(futures.size() + 1)
        BidModel maxBid = EMPTY_BID
        bids << EMPTY_BID
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

    /**
     * We will actually save the auction in the background.
     * This has the potential for issues if the #registerClick method is called before the task is executed.
     * However, the task should normally complete within a few hundred milliseconds and a click that fast would be fishy.
     * @param bids
     * @param winningBid
     * @param transactionId
     * @param userId
     * @return
     */
    def recordAuction(Collection<BidModel> bids, BidModel winningBid, String transactionId, int userId) {
        executorService.submit(new AuctionRecorder(bids: bids,
                winningBid: winningBid,
                transactionId: transactionId,
                userId: userId,
                providerService: providerService,
                userService: userService,
                auctionService: this,
                sessionFactory: sessionFactory
        ))
    }

    /**
     * Looks up an auction and registers a ClickResult of CLICK or STALE.
     * It is determined by adding the click timer to the auction time then comparing to the current time.
     * A possible future feature would be to check if the current click result is REQUEST and only update if so.
     * @param transactionId
     * @param userId
     * @return The ClickResult we registered.
     */
    def registerClick(String transactionId, int userId) {
        //TODO - What to do if userid mismatch, throw up for now, check for REQUEST?
        def auction = getAuction(transactionId)
        if (auction.user.userId == userId) {
            ClickResult result
            if (System.currentTimeMillis() < auction.performed.time + clickTimer) {
                result = ClickResult.CLICK
            } else {
                result = ClickResult.STALE
            }
            auction.clickResult = result
            saveAuction(auction)
            return result
        } else {
            throw new IllegalArgumentException("User ID does not match original transaction")
        }
    }
}
