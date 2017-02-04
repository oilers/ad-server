package org.oiler.ad.server.entities

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

}
