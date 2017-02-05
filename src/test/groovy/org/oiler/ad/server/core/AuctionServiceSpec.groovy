package org.oiler.ad.server.core

import groovy.util.logging.Slf4j
import org.oiler.ad.server.api.AuctionParamaters
import org.oiler.ad.server.api.BidModel
import org.oiler.ad.server.api.ProviderModel
import org.oiler.ad.server.db.AuctionDAO
import spock.lang.Specification

import javax.ws.rs.client.Client
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by Kodi on 2/4/2017.
 */
@Slf4j
class AuctionServiceSpec extends Specification {
    def maxDurationNano = 2.0e+8

    def "When an auction is performed there is a winner and it is recorded"() {
        AuctionDAO auctionDAO = Mock()
        Client client = Mock()
        ProviderService providerService = Mock()
        ExecutorService executorService = Mock()
        String uuid = UUID.randomUUID().toString()
        AuctionService auctionService = new AuctionService(auctionDAO: auctionDAO, client: client, providerService: providerService, executorService: executorService)
        AuctionParamaters paramaters = new AuctionParamaters(width: 100, height: 100, userId: 1, userAgent: "Spock Test", domain: "unittest.com", ip: "444.444.444.444")
        when:
        def start = System.nanoTime()
        def auctionResult = auctionService.performAuction(paramaters)
        def duration = System.nanoTime() - start
        then:
        1 * providerService.getProviderBySizeAndUser(100, 100, 1) >> { width, height, userId ->
            def result = []
            5.times {
                result.add([providerId: it, providerName: "Test Provider ${it}", url: "http://testprovider${it}.com/bid"] as ProviderModel)
            }
            return result
        }
        1 * executorService.invokeAll(_, 150, TimeUnit.MILLISECONDS) >> {
            def result = []
            5.times { num ->
                def future = Mock(Future)
                future.get() >> { [bidprice: 0.1 * num, adhtml: "<p>Ad #${num}</p>"] as BidModel }
                result.add(future)
            }
            return result
        }
        1 * executorService.submit(_)
        expect:
        auctionResult.html == "<p>Ad #4</p>"
        log.info("Took ${duration}")
    }

}
