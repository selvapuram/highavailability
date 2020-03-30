package com.optum.challenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.optum.challenge.exception.CustomException;
import com.optum.challenge.model.ResponseModel;
import com.optum.challenge.service.IPartitionStrategy;
import com.optum.challenge.service.impl.DefaultPartitionStrategy;

public class HighAvailablity {

    private ConcurrentHashMap<String, Set<String>> hosts;
    
    private static final HighAvailablity cluster = new HighAvailablity();
    
    private IPartitionStrategy partitionStrategy;

    // preventing constructor to access via static factory methods
    private HighAvailablity() {

    }
    
    private HighAvailablity initialize(int no_of_hosts, int no_of_maxfiles) {
		
		// feed input demo
        if(no_of_hosts == 0) {
            no_of_hosts = 5;
        }
        if(no_of_maxfiles == 0) {
            no_of_maxfiles = 10;
        }
		hosts = new ConcurrentHashMap<>();
		for(int i = 1; i <= no_of_hosts; i++) {
		    Random r = new Random();
		    int random = r.nextInt(no_of_maxfiles);
		    if(random == 0 || random == 1) {
		        random = 5;
		    }
			Set<String> files = GetRandomFiles.fileGen.getFiles(no_of_maxfiles, 1, random);
			hosts.put("host" + i, files);
		}
		return cluster;
	}
    
    private HighAvailablity demoinitializer() {
        
        hosts = new ConcurrentHashMap<>();
        String[] testFiles = new String[4];
        testFiles[0] = "file1,file2,file4,file5";
        testFiles[1] = "file2, file3";
        testFiles[2] = "file1, file3, file4";
        testFiles[3] = "file5";
        for(int i = 1; i <= 4; i++) {
            Random r = new Random();
            int random = r.nextInt((i+1)*20);
            if(random == 0 || random == 1) {
                random = 5;
            }
            String[] splitValues = testFiles[i - 1].split(",");
            Set<String> files = new HashSet<>(Arrays.asList(splitValues));
            hosts.put("host" + i, files);
        }
        return cluster;
    }
    
    public static HighAvailablity getCluster(int no_of_hosts, int no_of_files) {
        if(cluster.hosts != null) {
            return cluster;
        } else {
            return cluster.initialize(no_of_hosts, no_of_files);
        }
    }
    
    public static HighAvailablity getCluster() {
        if(cluster.hosts != null) {
            return cluster;
        } else {
            // cluster not initialized hence initializing 
            return cluster.initialize(10, 10);
        }
    }
    
    public static HighAvailablity getTestCluster() {
        if(cluster.hosts != null) {
            return cluster;
        } else {
            return cluster.demoinitializer();
        }
    }
    
    public static HighAvailablity getLargeCluster() {
        if(cluster.hosts != null) {
            return cluster;
        } else {
            return cluster.initialize(1000, 100000);
        }
    }
    
    public void setDefaultPartitionStrategy() {
        this.setPartitionStrategy(new DefaultPartitionStrategy());
    }
    
    public List<ResponseModel> doPartition(List<String> volatileHosts) {
        
        int size = volatileHosts.size();
        if(size > 2) {
            throw new IllegalArgumentException("Maximum two hosts allowed");
        }
        if(hosts == null) {
            throw new CustomException("Error: Hosts aren't initialized");
        }
        if(this.getPartitionStrategy() == null) {
            this.setDefaultPartitionStrategy();
        }
        return this.getPartitionStrategy().doPartition(volatileHosts);
    }

    public ConcurrentHashMap<String, Set<String>> getHosts() {
        return hosts;
    }

    public IPartitionStrategy getPartitionStrategy() {
        return partitionStrategy;
    }

    public void setPartitionStrategy(IPartitionStrategy partitionStrategy) {
        this.partitionStrategy = partitionStrategy;
    }
}
