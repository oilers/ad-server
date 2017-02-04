package org.oiler.ad.server.api

/**
 * Created by Kodi on 2/4/2017.
 */
class ProviderModel {
    int providerId
    String providerName
    String url
    Collection<AdSizeModel> adSizes
    Collection<UserModel> users
}
