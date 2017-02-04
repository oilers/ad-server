package org.oiler.ad.server.db

import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory
import org.oiler.ad.server.entities.User

/**
 * Created by Kodi on 2/4/2017.
 */
class UserDAO extends AbstractDAO<User> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory)
    }

    public User findById(int id) {
        return get(id)
    }

    public User save(User user) {
        return persist(user)
    }

    public Collection<User> findAll(){
        return list(namedQuery("org.oiler.ad.server.entities.User.findAll"))
    }
}
