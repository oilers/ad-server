package org.oiler.ad.server.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import org.oiler.ad.server.api.ProviderModel
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

    public Collection<Provider> findAll() {
        return list(namedQuery("org.oiler.ad.server.entities.Provider.findAll"))
    }

    public Collection<Provider> findBySize(int width, int height) {
        return list(namedQuery("org.oiler.ad.server.entities.Provider.findBySize").setParameter("width", width).setParameter("height", height))
    }

    public Collection<ProviderModel> findBySizeAndUser(int width, int height, int userId) {
        return list(namedQuery("org.oiler.ad.server.entities.Provider.findBySizeAndUser")
                .setParameter("width", width)
                .setParameter("height", height)
                .setParameter("userId", userId)
        ).collect { new ProviderModel(providerId: it[0], providerName: it[1], url: it[2]) }
    }
}
