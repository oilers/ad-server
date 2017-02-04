package org.oiler.ad.server.api

/**
 * Created by Kodi on 2/4/2017.
 */
class UserModel {
    int userId
    String username
    Collection<AdSizeModel> adSizes
    Collection<ProviderModel> providers
}
