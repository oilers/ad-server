package org.oiler.ad.server.core

import groovy.util.logging.Slf4j
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.context.internal.ManagedSessionContext
import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.entities.Auction
import org.oiler.ad.server.entities.Bid

/**
 * Created by Kodi on 2/4/2017.
 */
@Slf4j
class AuctionRecorder implements Runnable {
    Collection<BidModel> bids
    BidModel winningBid
    String transactionId
    int userId
    ProviderService providerService
    UserService userService
    AuctionService auctionService
    SessionFactory sessionFactory

    @Override
    void run() {
        Session session = sessionFactory.openSession()
        try {
            ManagedSessionContext.bind(session)
            Transaction transaction = session.beginTransaction()
            try {
                Auction auction = new Auction()
                auction.winningPrice = winningBid.bidprice
                auction.winningProvider = providerService.getProvider(winningBid.providerId)
                auction.transactionId = transactionId
                auction.user = userService.getUser(userId)
                auction.bids = bids.collect { model ->
                    def bid = Bid.fromModel(model, auction, providerService)
                    return bid
                }
                auctionService.saveAuction(auction)
                transaction.commit()
                log.info("Succesfully recorded auction ${transactionId}")
            } catch (Exception ex) {
                log.error("Unable to record auction: ", ex)
                transaction.rollback()
            }
        } finally {
            session.close()
            ManagedSessionContext.unbind(sessionFactory)
        }
    }
}
