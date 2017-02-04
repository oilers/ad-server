package org.oiler.ad.server.entities

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
@Table(name = "user_provider_assoc")
class UserProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_provider_assoc_id")
    int userProviderId
    @OneToOne
    @JoinColumn(name = "user_id")
    User user
    @OneToOne
    @JoinColumn(name = "provider_id")
    Provider provider
}
