package org.oiler.ad.server.core

import org.oiler.ad.server.entities.*
import org.oiler.ad.server.db.UserDAO

/**
 * Created by Kodi on 2/4/2017.
 */
class UserService {
    UserDAO userDAO

    User getUser(int id) {
        return userDAO.findById(id)
    }

    User saveUser(User user) {
        return userDAO.save(user)
    }

    User associateAdSize(User user, AdSize adSize) {
        user.userAdSizes.add(new UserAdSize(user: user, adSize: adSize))
        return saveUser(user)
    }

    User associateProvider(User user, Provider provider) {
        user.userProviders.add(new UserProvider(user: user, provider: provider))
        return saveUser(user)
    }

    Collection<User> getUsers() {
        return userDAO.findAll()
    }
}
