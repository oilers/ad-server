package org.oiler.ad.server.api
/**
 * Created by Kodi on 2/4/2017.
 *
 */
class AuctionModel {
    String transactionId
    int userid
    Collection<BidModel> bids
    double winningPrice
    int winningProvider
    ClickResult clickResult

}
