package com.optum.challenge.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.optum.challenge.HighAvailablity;
import com.optum.challenge.exception.CustomException;
import com.optum.challenge.model.ResponseModel;
import com.optum.challenge.service.IPartitionStrategy;

public class DefaultPartitionStrategy implements IPartitionStrategy {
    
    

    private int parellerThreshold;

    public int getParellerThreshold() {
        return parellerThreshold;
    }

    @Override
    public List<ResponseModel> doPartition(List<String> volatileHosts) {
        HighAvailablity cluster = getCluster();
        int size = volatileHosts.size();
        if (size > 2) {
            throw new IllegalArgumentException("Maximum two hosts allowed");
        }
        ConcurrentHashMap<String, Set<String>> clusterHosts = cluster.getHosts();
        if (clusterHosts == null) {
            throw new CustomException("Error: Hosts aren't initialized");
        }

        final List<ResponseModel> out = new ArrayList<>();
        final Queue<ResponseModel> queue = new LinkedBlockingQueue<>();
        for (String h : volatileHosts) {
            if (clusterHosts.containsKey(h)) {
                Set<String> files = clusterHosts.get(h);
                BiConsumer<String, Set<String>> biConsumer = (key, value) -> {
                    if (!key.equals(h)) { // excludes down host
                        files.stream().filter(e -> value.contains(e)).peek(f -> {
                            queue.offer(new ResponseModel(f, key, null));
                        }).count();
                    }
                };
                clusterHosts.forEach(getParellerThreshold(), biConsumer);
                AtomicReference<String> backupFile = new AtomicReference<>(null);
                AtomicReference<String> destinationHost = new AtomicReference<>(null);
                // iterate the through the till the queue gets empty and lookup hosts
                // severe high complexity, need to implement producer - consumer using blocking queue
                while (!queue.isEmpty()) {
                    ResponseModel result = queue.poll();
                    BiFunction<String, Set<String>, Set<String>> biFunction = (k, v) -> {
                        if (!k.equals(h) && !k.equals(result.getSourceHost()) && !k.equals(destinationHost.get())) {
                            destinationHost.set(k);
                            return v;
                        }
                        return null;
                    };
                    Set<String> v = clusterHosts.search(getParellerThreshold(), biFunction);
                    if(v == null && destinationHost.get() != null) {
                        v = clusterHosts.get(destinationHost.get());
                    }
                    if (v != null) {
                        v.add(backupFile.get());
                        out.add(new ResponseModel(result.getFileName(), result.getSourceHost(), destinationHost.get()));
                    }
                }

                // re-structures the input
                clusterHosts.remove(h);

            }

        }

        return out;
    }

    @Override
    public HighAvailablity getCluster() {
        return HighAvailablity.getCluster();
    }

    @Override
    public void setParellerThreshold(int threshold) {
        parellerThreshold = threshold;
    }
}
