package it.unimore.dipi.iot.dt.api.orchestration.persistence;

import it.unimore.dipi.iot.dt.api.orchestration.model.ConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.DigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtWorkerConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.exception.IoTInventoryDataManagerConflict;
import it.unimore.dipi.iot.dt.api.orchestration.exception.IoTInventoryDataManagerException;

import java.util.*;

/**
 *
 * Demo IoT Inventory Data Manager handling all data in a local cache implemented through Maps and Lists
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:48
 */
public class DefaultIotInventoryDataManger implements IIoInventoryDataManager{

    private HashMap<String, DigitalTwinDescriptor> digitalTwinMap;
    private HashMap<String, ConfigurationDescriptor> configurationMap;

    public DefaultIotInventoryDataManger(){
        this.digitalTwinMap = new HashMap<>();
        this.configurationMap = new HashMap<>();

    }

    //DIGITAL TWIN MANAGEMENT

    @Override
    public List<DigitalTwinDescriptor> getDigitalTwinList() throws IoTInventoryDataManagerException {
        return new ArrayList<>(this.digitalTwinMap.values());
    }

    @Override
    public Optional<DigitalTwinDescriptor> getDigitalTwinById(String digitalTwinId) throws IoTInventoryDataManagerException {
        return Optional.ofNullable(this.digitalTwinMap.get(digitalTwinId));
    }

    @Override
    public DigitalTwinDescriptor createNewDigitalTwin(DigitalTwinDescriptor digitalTwinDescripton) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {

        if(digitalTwinDescripton != null && this.getDigitalTwinById(digitalTwinDescripton.getId()).isPresent())
            throw new IoTInventoryDataManagerConflict("DigitalTwin with the same id already available!");

        //Set the digitalTwinId to a random UUID value
        if(digitalTwinDescripton.getId() == null)
            digitalTwinDescripton.setId(String.format("%s_%s", digitalTwinDescripton.getType(), UUID.randomUUID().toString()));

        this.digitalTwinMap.put(digitalTwinDescripton.getId(), digitalTwinDescripton);
        return digitalTwinDescripton;

    }

    @Override
    public DigitalTwinDescriptor updateDigitalTwin(DigitalTwinDescriptor digitalTwinDescripton) throws IoTInventoryDataManagerException {
        this.digitalTwinMap.put(digitalTwinDescripton.getId(), digitalTwinDescripton);
        return digitalTwinDescripton;
    }

    @Override
    public DigitalTwinDescriptor deleteDigitalTwin(String digitalTwinId) throws IoTInventoryDataManagerException {
        return this.digitalTwinMap.remove(digitalTwinId);
    }

    //CONFIGURATION MANAGEMENT

    @Override
    public Optional<ConfigurationDescriptor> getConfiguration(String configurationId) throws IoTInventoryDataManagerException {
        return Optional.ofNullable(this.configurationMap.get(configurationId));
    }

    @Override
    public ConfigurationDescriptor createNewConfiguration(ConfigurationDescriptor configurationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {
        if(configurationDescriptor != null && this.getConfiguration(configurationDescriptor.getConfId()).isPresent())
            throw new IoTInventoryDataManagerConflict("Configuration with the same id already available!");
        String confId = configurationDescriptor.getConfId();
        String dtId = confId.replace("conf", "dt");
        if(getDigitalTwinById(dtId).isPresent()){
            this.configurationMap.put(configurationDescriptor.getConfId(), configurationDescriptor);
        }
        else {
            throw new IoTInventoryDataManagerConflict("You can't create a cofniguration of a digital twin that not exists");
        }
        //this.configurationMap.put(configurationDescriptor.getConfId(), configurationDescriptor);
        return configurationDescriptor;
    }

    @Override
    public ConfigurationDescriptor updateConfiguration(ConfigurationDescriptor configurationDescriptor) throws IoTInventoryDataManagerException {
        this.configurationMap.put(configurationDescriptor.getConfId(), configurationDescriptor);
        return configurationDescriptor;
    }

    @Override
    public ConfigurationDescriptor deleteConfiguration(String configurationId) throws IoTInventoryDataManagerException {
        return this.configurationMap.remove(configurationId);
    }

    //WORKER MANAGEMENT

    @Override
    public List<WldtWorkerConfigurationDescriptor> getWorkerList(String confId) throws IoTInventoryDataManagerException {
        return this.configurationMap.get(confId).getConfValue().getWorkers();
    }

    @Override
    public WldtWorkerConfigurationDescriptor getWorkerById(String confId, String workerId) throws IoTInventoryDataManagerException {
        List<WldtWorkerConfigurationDescriptor> workers = getWorkerList(confId);
        Iterator<WldtWorkerConfigurationDescriptor> it = workers.iterator();
        WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor = new WldtWorkerConfigurationDescriptor();
        while (it.hasNext()) {
            wldtWorkerConfigurationDescriptor = it.next();
            if(wldtWorkerConfigurationDescriptor.getWorkerId().equals(workerId)){
                //return wldtWorkerConfigurationDescriptor;
                break;
            }
            else {
                wldtWorkerConfigurationDescriptor = null;
            }
        }
        return wldtWorkerConfigurationDescriptor;
    }

    @Override
    public WldtWorkerConfigurationDescriptor createNewWorker(String confId, WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {
        this.configurationMap.get(confId).getConfValue().addWorkerToList(wldtWorkerConfigurationDescriptor);
        return wldtWorkerConfigurationDescriptor;
    }

    @Override
    public WldtWorkerConfigurationDescriptor updateWorker(String confId, WldtWorkerConfigurationDescriptor oldWorker, WldtWorkerConfigurationDescriptor updatedWorker) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {
        deleteWorker(confId, oldWorker);
        createNewWorker(confId, updatedWorker);
        return updatedWorker;
    }

    @Override
    public WldtWorkerConfigurationDescriptor deleteWorker(String confId, WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor) throws IoTInventoryDataManagerException {
        this.configurationMap.get(confId).getConfValue().removeWorkerToList(wldtWorkerConfigurationDescriptor);
        return wldtWorkerConfigurationDescriptor;
    }

}
