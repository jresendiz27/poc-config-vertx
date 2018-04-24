package mx.jresendiz.poc.verticles;

import com.hazelcast.client.HazelcastClient;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import mx.jresendiz.poc.api.datastore.DataStoreOperations;
import mx.jresendiz.poc.impl.DataStoreOperationsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by jresendiz on 23/04/18.
 */
public class ConfigVerticle extends AbstractVerticle {
    private static Logger log = LoggerFactory.getLogger(ConfigVerticle.class);
    private DataStoreOperations dataStoreOperations;

    public DataStoreOperations getDataStoreOperations() {
        return dataStoreOperations;
    }

    public void setDataStoreOperations(DataStoreOperations dataStoreOperations) {
        this.dataStoreOperations = dataStoreOperations;
    }

    public void start() {
        log.info("ConfigVerticleId: {}", deploymentID());
        initialize();
    }

    private void initialize() {
        dataStoreOperations = new DataStoreOperationsImpl();
        dataStoreOperations.setHazelcastInstance(HazelcastClient.newHazelcastClient());

        ConfigStoreOptions gitStore = new ConfigStoreOptions()
                .setType("git")
                .setConfig(new JsonObject()
                        .put("url", "https://github.com/jresendiz27/Api-Tweets-Config.git")
                        .put("path", ".local-config")
                        .put("filesets",
                                new JsonArray().add(new JsonObject().put("pattern", "*.json"))));

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .setScanPeriod(2000)
                .addStore(gitStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.getConfig(event -> {
            if (event.succeeded()) {
                log.debug("Configuration retrieved ... {}", event.result().encode());
                JsonObject remoteConfig = event.result();
                deployVerticles(remoteConfig);
            } else {
                vertx.eventBus().send("config.retriever.error", event.failed());
            }
        });
        retriever.listen(event -> {
            JsonObject newConfig = event.getNewConfiguration();
            undeployVerticles(newConfig);
            deployVerticles(newConfig);
        });

    }

    private void undeployVerticles(JsonObject config) {
        log.info(">> Undeploy verticles ...");
        log.info("CurrentVerticles: {}", vertx.deploymentIDs());
        config.getJsonArray("Verticles").forEach((objectEntry) -> {
            JsonObject jsonObject = (JsonObject) objectEntry;
            Set<String> verticles = dataStoreOperations.getDeployedVerticlesFromClass(jsonObject.getString("name"));
            log.info("DeploymentsIds: {}", vertx.deploymentIDs());
            if (verticles != null) {
                verticles.forEach(verticleId ->
                        vertx.undeploy(
                                verticleId,
                                handler -> {
                                    if (handler.succeeded()) {
                                        log.info("Verticle with ID {}, undeployed ...", verticleId);
                                        dataStoreOperations.removeVerticleIdFromClass(jsonObject.getString("name"), verticleId);
                                    }
                                }));
            }
        });
    }

    private void deployVerticles(JsonObject config) {
        log.info(">> Deploy verticles ...");
        log.info("CurrentVerticles: {}", vertx.deploymentIDs());
        config.getJsonArray("Verticles").forEach((objectEntry) -> {
            JsonObject jsonObject = (JsonObject) objectEntry;
            DeploymentOptions deploymentOptions = getDeploymentOptions(jsonObject);
            vertx.deployVerticle(
                    jsonObject.getString("name"),
                    deploymentOptions,
                    handler -> {
                        String deploymentId = handler.result();
                        dataStoreOperations.setDeployedVerticle(jsonObject.getString("name"), deploymentId);
                    });
        });
    }

    private DeploymentOptions getDeploymentOptions(JsonObject jsonObject) {
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(jsonObject.getInteger("instances"));
        deploymentOptions.setConfig(jsonObject.getJsonObject("config"));
        return deploymentOptions;
    }
}
