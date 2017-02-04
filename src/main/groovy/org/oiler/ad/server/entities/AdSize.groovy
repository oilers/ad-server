package org.oiler.ad.server.entities

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
class AdSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_size_id")
    int adSizeId
    @Column(nullable = false)
    int width
    @Column(nullable = false)
    int height
}
