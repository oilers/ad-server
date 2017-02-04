package org.oiler.ad.server.entities

import org.oiler.ad.server.api.ProviderModel

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
@NamedQueries([@NamedQuery(name = "org.oiler.ad.server.entities.Provider.findAll", query = "SELECT provider from Provider provider"),
        @NamedQuery(name = "org.oiler.ad.server.entities.Provider.findBySize",
                query = """select 
                             distinct pas.provider 
                          from 
                            AdSize adSize,  
                            ProviderAdSize pas
                          where
                            adSize.width = :width 
                            and adSize.height = :height
                             """)
])
@NamedNativeQueries([@NamedNativeQuery(name = "org.oiler.ad.server.entities.Provider.findBySizeAndUser", query =
        """
SELECT
  DISTINCT provider.provider_id,
  provider_name,
  url
FROM
  user
  JOIN user_provider_assoc ON user.user_id = user_provider_assoc.user_id
  JOIN user_size_assoc ON user_provider_assoc.user_id = user_size_assoc.user_id
  JOIN ad_size ON user_size_assoc.ad_size_id = ad_size.ad_size_id
  JOIN provider_size_assoc ON ad_size.ad_size_id = provider_size_assoc.provider_id
  JOIN provider ON provider_size_assoc.provider_id = provider.provider_id
WHERE
  width = :width AND
  height = :height AND
  user.user_id = :userId
""")])
class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id")
    int providerId
    @Column(name = "provider_name", length = 64)
    String providerName
    @Column(length = 512)
    String url
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    Collection<ProviderAdSize> providerAdSizes
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    Collection<UserProvider> userProviders


    public ProviderModel toModel(boolean includeUsers = true) {
        ProviderModel model = new ProviderModel()
        model.providerId = providerId
        model.providerName = providerName
        model.url = url
        model.adSizes = providerAdSizes.collect { it.adSize.toModel() }
        if (includeUsers) {
            model.users = userProviders.collect { it.user.toModel(false) }
        }
        return model
    }
}
