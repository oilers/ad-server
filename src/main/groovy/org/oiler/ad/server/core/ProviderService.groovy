package org.oiler.ad.server.core

import org.oiler.ad.server.entities.*
import org.oiler.ad.server.db.ProviderDAO

/**
 * Created by Kodi on 2/4/2017.
 */
class ProviderService {
    ProviderDAO providerDAO

    Provider getProvider(int id) {
        return providerDAO.findById(id)
    }

    Provider saveProvider(Provider user) {
        return providerDAO.save(user)
    }

    Provider associateAdSize(Provider provider, AdSize adSize) {
        provider.providerAdSizes.add(new ProviderAdSize(provider: provider, adSize: adSize))
        return saveProvider(provider)
    }

    Provider associateUser(Provider provider, User user) {
        user.userProviders.add(new UserProvider(provider: provider, user: user))
        return saveProvider(provider)
    }

}