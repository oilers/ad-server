package org.oiler.ad.server.entities

import org.oiler.ad.server.api.UserModel

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId
    @Column(unique = true, length = 100)
    String username
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    Collection<UserAdSize> userAdSizes
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    Collection<UserProvider> userProviders

    UserModel toModel(boolean includeProviders = true) {
        UserModel model = new UserModel()
        model.username = username
        model.userId = userId
        model.adSizes = userAdSizes.collect{it.adSize.toModel()}
        if(includeProviders) {
            model.providers = userProviders.collect{it.provider.toModel(false)}
        }
        return model
    }
}
