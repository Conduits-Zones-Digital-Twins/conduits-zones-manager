package it.unimore.dipi.iot.dt.utils;

import it.unimore.dipi.iot.dt.api.orchestration.model.*;
import it.unimore.dipi.iot.dt.api.orchestration.exception.IoTInventoryDataManagerException;
import it.unimore.dipi.iot.dt.api.orchestration.persistence.IIoInventoryDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 12:04
 */
public class DummyDataGenerator {

    public static void generateDummyDigitalTwins(IIoInventoryDataManager inventoryDataManager) throws IoTInventoryDataManagerException {

        try {
            DigitalTwinDescriptor digitalTwinDescripton00001 = new DigitalTwinDescriptor();
            digitalTwinDescripton00001.setId("dt00001");
            digitalTwinDescripton00001.setType("MQTT");
            digitalTwinDescripton00001.setRunning(true);

            DigitalTwinDescriptor digitalTwinDescriptor00002 = new DigitalTwinDescriptor();
            digitalTwinDescriptor00002.setId("dt00002");
            digitalTwinDescriptor00002.setType("COAP");
            digitalTwinDescriptor00002.setRunning(true);

            inventoryDataManager.createNewDigitalTwin(digitalTwinDescripton00001);
            inventoryDataManager.createNewDigitalTwin(digitalTwinDescriptor00002);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void generateDummyConfigurations(IIoInventoryDataManager inventoryDataManager) throws IoTInventoryDataManagerException {

        try {

            //Conf for dt mqtt

            HashMap<String,Object> engineMap00001 = new HashMap<String,Object>(){{
                put("threadPoolSize", 5);
                put("deviceNameSpace", "it.unimore.dipi.things");
                put("wldtBaseIdentifier","wldt");
                put("wldtStartupTimeSeconds", 10);
                put("activeProtocolList", "mqtt");
                put("applicationMetricsEnabled", false);
                put("applicationMetricsReportingPeriodSeconds", 10);
                put("metricsReporterList", "csv, graphite");
                put("graphiteReporterAddress", "127.0.0.1");
                put("graphiteReporterPort", 2003);
                put("graphitePrefix", "wldt");
            }};

            HashMap<String,Object> workerMap00001 = new HashMap<String,Object>(){{
                put("dtForwardingEnabled", true);
                put("dtPublishingQoS", 0);
                put("brokerAddress","127.0.0.1");
                put("brokerPort", 1883);
                put("brokerLocal", true);
                put("brokerClientUsername", null);
                put("brokerClientPassword", null);
                put("brokerSecureCommunicationRequired", false);
                put("brokerServerCertPath", null);
                put("destinationBrokerAddress", "127.0.0.1");
                put("destinationBrokerPort", 1884);
                put("destinationBrokerLocal", true);
                put("destinationBrokerClientUsername", null);
                put("destinationBrokerClientPassword", null);
                put("destinationBrokerSecureCommunicationRequired", false);
                put("destinationBrokerServerCertPath", null);
                put("destinationBrokerBaseTopic", null);
                put("deviceId", "it.unimore.dipi.things:dummy_device:0be64e3d-985d-43f2-b810-10e8e1d6862a");
                put("topicList", null);
            }};

            WldtEngineConfigurationDescriptor engine = new WldtEngineConfigurationDescriptor(engineMap00001);
            WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor00001 = new WldtWorkerConfigurationDescriptor("worker00001","MQTT2MQTT",workerMap00001);
            List<WldtWorkerConfigurationDescriptor> workers = new ArrayList<>();
            workers.add(wldtWorkerConfigurationDescriptor00001);
            WldtConfigurationDescriptor wldtConfigurationDescriptor = new WldtConfigurationDescriptor(engine, workers);
            ConfigurationDescriptor configurationDescriptor00001 = new ConfigurationDescriptor("conf00001", "wldt", wldtConfigurationDescriptor);

            inventoryDataManager.createNewConfiguration(configurationDescriptor00001);


        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
