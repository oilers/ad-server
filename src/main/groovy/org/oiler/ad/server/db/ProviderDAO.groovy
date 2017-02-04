package org.oiler.ad.server.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import org.oiler.ad.server.entities.Provider

/**
 * Created by Kodi on 2/4/2017.
 */
class ProviderDAO extends AbstractDAO<Provider> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    ProviderDAO(SessionFactory sessionFactory) {
        super(sessionFactory)
    }

    public Provider findById(int id) {
        return get(id)
    }

    public Provider save(Provider provider) {
        return persist(provider)
    }

    public Collection<Provider> findAll(){
        return list(namedQuery("org.oiler.ad.server.entities.Provider.findAll"))
    }
}
