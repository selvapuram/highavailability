package com.optum.challenge.model;

public class ResponseModel {
    @Override
    public String toString() {
        return fileName + ", " + sourceHost + ", " + destinationHost;
    }

    private String fileName;
    
    private String sourceHost;
    
    private String destinationHost;

    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     * @param sourceHost
     * @param destinationHost
     */
    public ResponseModel(String fileName, String sourceHost, String destinationHost) {
        super();
        this.fileName = fileName;
        this.sourceHost = sourceHost;
        this.destinationHost = destinationHost;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(String destinationHost) {
        this.destinationHost = destinationHost;
    }
}
