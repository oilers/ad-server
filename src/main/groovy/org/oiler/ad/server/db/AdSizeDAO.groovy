package org.oiler.ad.server.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import org.oiler.ad.server.entities.AdSize

/**
 * Created by Kodi on 2/4/2017.
 */
class AdSizeDAO extends AbstractDAO<AdSize> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    AdSizeDAO(SessionFactory sessionFactory) {
        super(sessionFactory)
    }

    public AdSize findById(int id) {
        return get(id)
    }

    public AdSize save(AdSize user) {
        return persist(user)
    }

    public Collection<AdSize> findAll() {
        return list(namedQuery("org.oiler.ad.server.entities.AdSize.findAll"))
    }
}
