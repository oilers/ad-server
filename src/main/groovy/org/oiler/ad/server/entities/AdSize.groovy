package org.oiler.ad.server.entities

import org.oiler.ad.server.api.AdSizeModel

import javax.persistence.*

/**
 * Created by Kodi on 2/4/2017.
 */
@Entity
@Table(name = "ad_size")
class AdSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_size_id")
    int adSizeId
    @Column(nullable = false)
    int width
    @Column(nullable = false)
    int height

    AdSizeModel toModel() {
        AdSizeModel model = new AdSizeModel()
        model.adSizeId = adSizeId
        model.width = width
        model.height = height
        return model
    }
}
