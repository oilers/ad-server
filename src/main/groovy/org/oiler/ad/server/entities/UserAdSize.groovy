package org.oiler.ad.server.entities

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
@Table(name = "user_size_assoc")
class UserAdSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_size_assoc_id")
    int userAdSizeId
    @OneToOne
    @JoinColumn(name = "user_id")
    User user
    @OneToOne
    @JoinColumn(name = "ad_size_id")
    AdSize adSize
}
