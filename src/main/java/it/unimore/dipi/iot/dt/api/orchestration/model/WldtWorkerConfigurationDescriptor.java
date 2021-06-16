package it.unimore.dipi.iot.dt.api.orchestration.model;

import java.util.HashMap;

public class WldtWorkerConfigurationDescriptor {

    private String workerId;
    private String workerType;
    HashMap<String,Object> attributes;

    public WldtWorkerConfigurationDescriptor() {
    }

    public WldtWorkerConfigurationDescriptor(String workerId, String workerType, HashMap<String, Object> attributes) {
        this.workerId = workerId;
        this.workerType = workerType;
        this.attributes = attributes;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerType() {
        return workerType;
    }

    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "WldtWorkerConfigurationDescriptor{" +
                ", workerId='" + workerId + '\'' +
                ", type='" + workerType + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
