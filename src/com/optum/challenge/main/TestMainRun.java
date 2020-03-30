package com.optum.challenge.main;

import java.util.ArrayList;
import java.util.List;

import com.optum.challenge.HighAvailablity;
import com.optum.challenge.model.ResponseModel;

public class TestMainRun {

    public static void main(String[] args) {
        //HighAvailablity cluster = HighAvailablity.getCluster(10, 10);
        HighAvailablity cluster = HighAvailablity.getTestCluster();
        // sample input
        List<String> volatileHosts = new ArrayList<>();
        if (args[0].equals("default")) {
            volatileHosts.add("host2");
        } else {
            if (cluster.getHosts().keys().hasMoreElements()) {
                volatileHosts.add(cluster.getHosts().keys().nextElement());
            }
        }
        List<ResponseModel> out = cluster.doPartition(volatileHosts);
        out.stream().forEach(System.out::println);
    }

}
