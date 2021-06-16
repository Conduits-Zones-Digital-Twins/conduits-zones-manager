package it.unimore.dipi.iot.dt.utils;

import it.unimore.dipi.iot.dt.api.orchestration.model.ConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtEngineConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtWorkerConfigurationDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {

    public static void main(String args[]) {
        HashMap<String,Object> engineMap00001 = new HashMap<String,Object>(){{
            put("attr0","val00001");
            put("attr1","val00002");
            put("attr3",2);
        }};
        HashMap<String,Object> workerMap00001 = new HashMap<String,Object>(){{
            put("attr0","val00001");
            put("attr1","val00002");
            put("attr3",2);
        }};
        HashMap<String,Object> workerMap00002 = new HashMap<String,Object>(){{
            put("attr0","val00001");
            put("attr1","val00002");
            put("attr3",2);
        }};

        WldtEngineConfigurationDescriptor wldtEngineConfigurationDescriptor00001 = new WldtEngineConfigurationDescriptor(engineMap00001);
        WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor00001 = new WldtWorkerConfigurationDescriptor("worker00001","MQTT2MQTT",workerMap00001);
        WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor00002 = new WldtWorkerConfigurationDescriptor("worker00002","MQTT2COAP",workerMap00002);
        List<WldtWorkerConfigurationDescriptor> workers = new ArrayList<>();
        workers.add(wldtWorkerConfigurationDescriptor00001);
        workers.add(wldtWorkerConfigurationDescriptor00002);
        WldtConfigurationDescriptor wldtConfigurationDescriptor = new WldtConfigurationDescriptor(wldtEngineConfigurationDescriptor00001, workers);

        ConfigurationDescriptor configurationDescriptor = new ConfigurationDescriptor("conf00001", "wldt", wldtConfigurationDescriptor);
        ConfigurationDescriptor configurationDescriptor2 = new ConfigurationDescriptor("conf00001", "other", wldtConfigurationDescriptor);

        System.out.println(configurationDescriptor);
        System.out.println(configurationDescriptor2);

    }
}
