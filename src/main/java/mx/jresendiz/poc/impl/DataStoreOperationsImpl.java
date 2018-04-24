package mx.jresendiz.poc.impl;

import com.hazelcast.core.HazelcastInstance;
import mx.jresendiz.poc.api.datastore.DataStoreOperations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jresendiz on 23/04/18.
 */
public class DataStoreOperationsImpl implements DataStoreOperations {
    private final static String VERTICLES_MAP = "VERTICLES_MAP";
    private HazelcastInstance hazelcastInstance;
    private ConcurrentMap<String, Set<String>> verticlesMap;

    @Override
    public void initialize() {

    }

    @Override
    public void setDeployedVerticle(String className, String deploymentId) {
        verticlesMap = hazelcastInstance.getMap(VERTICLES_MAP);
        if (verticlesMap.get(className) != null) {
            verticlesMap.get(className).add(deploymentId);
        } else {
            Set<String> deploymentsId = new HashSet<>();
            deploymentsId.add(deploymentId);
            verticlesMap.put(className, deploymentsId);
        }
    }

    @Override
    public void removeVerticleIdFromClass(String className, String deploymentId) {
        verticlesMap = hazelcastInstance.getMap(VERTICLES_MAP);
        verticlesMap.get(className).remove(deploymentId);
    }

    @Override
    public Set<String> getDeployedVerticlesFromClass(String className) {
        verticlesMap = hazelcastInstance.getMap(VERTICLES_MAP);
        return verticlesMap.get(className);
    }

    @Override
    public Map<String, Set<String>> getAllDeployedVerticles() {
        return verticlesMap;
    }

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
