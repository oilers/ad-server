package org.oiler.ad.server.entities

import org.eclipse.jetty.util.annotation.Name
import org.oiler.ad.server.api.ProviderModel

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
@NamedQueries([@NamedQuery(name = "org.oiler.ad.server.entities.Provider.findAll", query = "SELECT provider from Provider provider"),
//@NamedQuery(name = "org.oiler.ad.server.entities.Provider.findByUserAndSize", query = "select provider from ")
])
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


    public ProviderModel toModel(boolean includeUsers=true){
        ProviderModel model = new ProviderModel()
        model.providerId = providerId
        model.providerName = providerName
        model.url = url
        model.adSizes = providerAdSizes.collect{it.adSize.toModel()}
        if(includeUsers) {
            model.users = userProviders.collect { it.user.toModel(false) }
        }
        return model
    }
}
