package org.oiler.ad.server.entities

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
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
}
