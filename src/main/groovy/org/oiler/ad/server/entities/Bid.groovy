package org.oiler.ad.server.entities

import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.core.ProviderService

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    int bidId
    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id")
    Provider provider
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    Auction auction
    @Column(name = "ad_html")
    String adHtml
    @Column(name = "bid_price")
    double bidPrice

    static Bid fromModel(BidModel bidModel, Auction auction, ProviderService providerService) {
        Bid bid = new Bid()
        bid.provider = providerService.getProvider(bidModel.providerId)
        bid.adHtml = bidModel.adhtml
        bid.bidPrice = bidModel.bidprice
        bid.auction = auction
        return bid
    }

    BidModel toModel(boolean includeAd = true) {
        BidModel bidModel = new BidModel()
        bidModel.providerId = provider.providerId
        if (includeAd) {
            bidModel.adhtml = adHtml
        }
        bidModel.bidprice = bidPrice
        return bidModel
    }
}
