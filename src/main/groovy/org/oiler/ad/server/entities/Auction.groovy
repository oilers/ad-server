package org.oiler.ad.server.entities

import org.oiler.ad.server.api.AuctionModel
import org.oiler.ad.server.api.ClickResult

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 *
 */
@Entity
@NamedQueries([@NamedQuery(name = "org.oiler.ad.server.entities.Auction.findAll", query = "SELECT auction from Auction auction")])
class Auction {
    @Id
    @Column(name = "transaction_id")
    String transactionId
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    Collection<Bid> bids
    @Column(name = "winning_price")
    Double winningPrice
    @ManyToOne
    @JoinColumn(name = "winning_provider_id")
    Provider winningProvider
    @Column
    Date performed
    @Column(name = "click_result")
    @Enumerated(EnumType.STRING)
    ClickResult clickResult = ClickResult.REQUEST

    AuctionModel toModel() {
        AuctionModel model = new AuctionModel()
        model.transactionId = transactionId
        model.bids = bids.collect { it.toModel() }
        model.userid = user.userId
        model.winningPrice = winningPrice
        model.winningProvider = winningProvider.providerId
        model.clickResult = clickResult
        return model
    }

}
