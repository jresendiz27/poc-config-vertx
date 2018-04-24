package mx.jresendiz.poc.api.datastore;

import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Set;

/**
 * Created by jresendiz on 23/04/18.
 */
public interface DataStoreOperations {
    void initialize();
    void setDeployedVerticle(String className, String deploymentId);
    void removeVerticleIdFromClass(String className, String deploymentId);
    Set<String> getDeployedVerticlesFromClass(String className);
    Map<String, Set<String>> getAllDeployedVerticles();

    void setHazelcastInstance(HazelcastInstance hazelcastInstance);
    HazelcastInstance getHazelcastInstance();
}
