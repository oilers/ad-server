package org.oiler.ad.server.core

import org.oiler.ad.server.entities.AdSize
import org.oiler.ad.server.db.AdSizeDAO

/**
 * Created by Kodi on 2/4/2017.
 */
class AdSizeService {
    AdSizeDAO userDAO

    AdSize getAdSize(int id) {
        return adSizeDAO.findById(id)
    }

    AdSize saveAdSize(AdSize adSize) {
        return adSizeDAO.save(adSize)
    }

}
