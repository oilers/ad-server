package org.oiler.ad.server.entities

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
@Table(name = "provider_size_assoc")
class ProviderAdSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_size_assoc_id")
    int userAdSizeId
    @OneToOne
    @JoinColumn(name = "provider_id")
    Provider provider
    @OneToOne
    @JoinColumn(name = "ad_size_id")
    AdSize adSize
}
