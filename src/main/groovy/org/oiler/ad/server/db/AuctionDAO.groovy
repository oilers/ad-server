package org.oiler.ad.server.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import org.oiler.ad.server.entities.Auction

/**
 * Created by Kodi on 2/4/2017.
 */
class AuctionDAO extends AbstractDAO<Auction> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    AuctionDAO(SessionFactory sessionFactory) {
        super(sessionFactory)
    }


    public Auction findById(String id) {
        return get(id)
    }

    public Auction save(Auction user) {
        return persist(user)
    }

    public Collection<Auction> findAll() {
        return list(namedQuery("org.oiler.ad.server.entities.Auction.findAll").setMaxResults(100))
    }
}
