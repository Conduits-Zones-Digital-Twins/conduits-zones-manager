package it.unimore.dipi.iot.dt.api.orchestration.model;

import java.util.List;

public class WldtConfigurationDescriptor{

    private WldtEngineConfigurationDescriptor engine;
    private List<WldtWorkerConfigurationDescriptor> workers;

    public WldtConfigurationDescriptor() {
    }

    public WldtConfigurationDescriptor(WldtEngineConfigurationDescriptor wldtEngineConfigurationDescriptor, List<WldtWorkerConfigurationDescriptor> workers) {
        this.engine = wldtEngineConfigurationDescriptor;
        this.workers = workers;
    }

    public WldtEngineConfigurationDescriptor getEngine() {
        return engine;
    }

    public void setEngine(WldtEngineConfigurationDescriptor engine) {
        this.engine = engine;
    }

    public List<WldtWorkerConfigurationDescriptor> getWorkers() {
        return workers;
    }

    public void setWorkers(List<WldtWorkerConfigurationDescriptor> workers) {
        this.workers = workers;
    }

    public void addWorkerToList(WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor) {
        this.workers.add(wldtWorkerConfigurationDescriptor);
    }

    public void removeWorkerToList(WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor){
        this.workers.remove(wldtWorkerConfigurationDescriptor);
    }

    @Override
    public String toString() {
        return "WldtConfigurationDescriptor{" +
                "wldtEngineConfigurationDescriptor=" + engine +
                ", workers=" + workers +
                '}';
    }

}
