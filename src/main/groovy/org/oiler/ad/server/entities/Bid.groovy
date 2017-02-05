package org.oiler.ad.server.entities

import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.core.ProviderService

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    int bidId
    @OneToOne
    @PrimaryKeyJoinColumn
    Provider provider
    @Column(name = "ad_html")
    String adHtml
    @Column(name = "bid_price")
    double bidPrice

    static Bid fromModel(BidModel bidModel, ProviderService providerService) {
        Bid bid = new Bid()
        bid.provider = providerService.getProvider(bidModel.providerId)
        bid.adHtml = bidModel.adhtml
        bid.bidPrice = bidModel.bidprice
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
