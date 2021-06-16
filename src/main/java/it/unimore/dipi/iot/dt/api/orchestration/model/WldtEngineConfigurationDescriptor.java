package it.unimore.dipi.iot.dt.api.orchestration.model;

import java.util.HashMap;

public class WldtEngineConfigurationDescriptor {

    private HashMap<String, Object> attributes = new HashMap<String, Object>() {{

    }};

    public WldtEngineConfigurationDescriptor() {
    }

    public WldtEngineConfigurationDescriptor(HashMap<String, Object> attributes) {

        this.attributes = attributes;

    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "WldtEngineConfigurationDescriptor{" +
                "attributes=" + attributes +
                '}';
    }

}
