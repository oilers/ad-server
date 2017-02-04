package org.oiler.ad.server.api

/**
 * Created by Kodi on 2/4/2017.
 */
class AdTransaction {
    String transactionId
    User user
    Collection<Bid> bids
    Double winningPrice
    Provider winningProvider
    ClickResult clickResult = ClickResult.REQUEST

}
