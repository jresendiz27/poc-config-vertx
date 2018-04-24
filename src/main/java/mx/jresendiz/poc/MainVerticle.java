package mx.jresendiz.poc;

import io.vertx.core.AbstractVerticle;
import mx.jresendiz.poc.verticles.ConfigVerticle;
import mx.jresendiz.poc.verticles.RestServerVerticle;
import mx.jresendiz.poc.verticles.WebClientVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
    private static Logger log = LoggerFactory.getLogger(MainVerticle.class);

    public void start() {
        vertx.deployVerticle(new ConfigVerticle());
        log.info("MainVerticleID: {}", deploymentID());
    }
}
