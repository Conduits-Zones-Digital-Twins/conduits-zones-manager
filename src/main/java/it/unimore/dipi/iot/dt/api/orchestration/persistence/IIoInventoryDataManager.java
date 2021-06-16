package it.unimore.dipi.iot.dt.api.orchestration.persistence;

import it.unimore.dipi.iot.dt.api.orchestration.model.ConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.DigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtWorkerConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.exception.IoTInventoryDataManagerConflict;
import it.unimore.dipi.iot.dt.api.orchestration.exception.IoTInventoryDataManagerException;

import java.util.List;
import java.util.Optional;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:44
 */
public interface IIoInventoryDataManager {

    //DigitalTwin Management

    public List<DigitalTwinDescriptor> getDigitalTwinList() throws IoTInventoryDataManagerException;

    public Optional<DigitalTwinDescriptor> getDigitalTwinById(String digitalTwinId) throws IoTInventoryDataManagerException;

    public DigitalTwinDescriptor createNewDigitalTwin(DigitalTwinDescriptor digitalTwinDescripton) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public DigitalTwinDescriptor updateDigitalTwin(DigitalTwinDescriptor digitalTwinDescripton) throws IoTInventoryDataManagerException;

    public DigitalTwinDescriptor deleteDigitalTwin(String digitalTwinId) throws IoTInventoryDataManagerException;

    //Configuration management, under Digital Twin

    public Optional<ConfigurationDescriptor> getConfiguration(String configurationId) throws IoTInventoryDataManagerException;

    public ConfigurationDescriptor createNewConfiguration(ConfigurationDescriptor configurationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public ConfigurationDescriptor updateConfiguration(ConfigurationDescriptor configurationDescriptor) throws IoTInventoryDataManagerException;

    public ConfigurationDescriptor deleteConfiguration(String configurationId) throws IoTInventoryDataManagerException;

    //worker management

    public List<WldtWorkerConfigurationDescriptor> getWorkerList(String confId) throws IoTInventoryDataManagerException;

    public WldtWorkerConfigurationDescriptor getWorkerById(String confId, String workerId) throws IoTInventoryDataManagerException;

    public WldtWorkerConfigurationDescriptor createNewWorker(String confId, WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public WldtWorkerConfigurationDescriptor updateWorker(String confId, WldtWorkerConfigurationDescriptor oldWorker, WldtWorkerConfigurationDescriptor updatedWorker) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public WldtWorkerConfigurationDescriptor deleteWorker(String confId, WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor) throws IoTInventoryDataManagerException;

}
