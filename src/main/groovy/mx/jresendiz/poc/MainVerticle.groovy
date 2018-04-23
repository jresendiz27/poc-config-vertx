package mx.jresendiz.poc

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.vertx.core.AbstractVerticle

/**
 * Created by jresendiz on 23/04/18.
 */
@Slf4j
@CompileStatic
class MainVerticle extends AbstractVerticle {
    void start() {
        // TODO Create Config Server
        // TODO Create Rest Server
        // TODO Register all events in the event-bus
        log.info("Starting verticle")
        log.info("Starting verticle")
    }
}
