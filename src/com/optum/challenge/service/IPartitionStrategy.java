package com.optum.challenge.service;

import java.util.List;

import com.optum.challenge.HighAvailablity;
import com.optum.challenge.model.ResponseModel;

public interface IPartitionStrategy {
    List<ResponseModel> doPartition(List<String> volatileHosts);
    
    HighAvailablity getCluster();
    
    void setParellerThreshold(int threshold);
}
