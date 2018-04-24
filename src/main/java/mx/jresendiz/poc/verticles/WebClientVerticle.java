package mx.jresendiz.poc.verticles;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jresendiz on 23/04/18.
 */
public class WebClientVerticle extends AbstractVerticle {
    private static Logger log = LoggerFactory.getLogger(WebClientVerticle.class);

    public void start() {
        log.info("WebClientVerticleId: {}", deploymentID());
        log.info("Config: {}", config().encode());
    }
}
