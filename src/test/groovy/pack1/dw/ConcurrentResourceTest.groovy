package pack1.dw

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

import static pack1.dw.CommonSpec.after
import static pack1.dw.CommonSpec.beforeRun

class ConcurrentResourceTest extends Specification {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentResourceTest.class)
    private static final int COUNTS = 25

    def setupSpec() {
        beforeRun()
    }

    def cleanupSpec() {
        after()
    }

    def common = new CommonSpec()

    def setup() {
        common.createTarget()
    }

    def "concurrent post get"() {
        def account = 2222
        given:
        "Account: " + account
        (0..10).each { int it ->
            account += it
            ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.runtime.availableProcessors() + 1)
            final AtomicBoolean success = new AtomicBoolean(true)
            try {
                final CountDownLatch latch = new CountDownLatch(COUNTS)
                for (int i = 0; i < COUNTS; i++) {
                    threadPool.execute({
                        try {
                            common.post_get(account)
                        } catch (Exception e) {
                            success.set(false)
                            throw new RuntimeException(e)
                        } finally {
                            latch.countDown()
                        }
                    })
                }
                try {
                    latch.await()
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e)
                    success.set(false)
                    Thread.currentThread().interrupt()
                }
            } finally {
                threadPool.shutdown()
            }
            if (success.get()) {
                log.info("CompletedWithNoErrors")
            } else {
                log.info("CompletedWithErrors")
            }
        }
    }
}
