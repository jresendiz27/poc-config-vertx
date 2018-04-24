package mx.jresendiz.poc.verticles;

import io.vertx.core.AbstractVerticle;
import mx.jresendiz.poc.MainVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jresendiz on 23/04/18.
 */
public class RestServerVerticle extends AbstractVerticle {
    private static Logger log = LoggerFactory.getLogger(RestServerVerticle.class);

    public void start() {
        log.info("RestServerVerticleId: {}", deploymentID());
        log.info("Config: {}", config());
    }
}
