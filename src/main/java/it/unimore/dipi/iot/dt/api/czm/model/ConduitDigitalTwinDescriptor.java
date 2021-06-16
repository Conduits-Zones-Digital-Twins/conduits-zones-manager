package it.unimore.dipi.iot.dt.api.czm.model;

import it.unimore.dipi.iot.dt.api.orchestration.model.DigitalTwinDescriptor;

import java.util.Map;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project conduits-zones-manager
 * @created 04/06/2021 - 12:10
 */
public class ConduitDigitalTwinDescriptor extends DigitalTwinDescriptor {

    private String conduitId;

    private ZoneDigitalTwinDescriptor firstZoneDigitalTwinDescriptor;

    private ZoneDigitalTwinDescriptor secondZoneDigitalTwinDescriptor;

    private Map<String, ConduitConfigurationParameter> attributes;

    public ConduitDigitalTwinDescriptor(String conduitId, ZoneDigitalTwinDescriptor firstZoneDigitalTwinDescriptor,
                                        ZoneDigitalTwinDescriptor secondZoneDigitalTwinDescriptor,
                                        Map<String, ConduitConfigurationParameter> attributes) {
        this.conduitId = conduitId;
        this.firstZoneDigitalTwinDescriptor = firstZoneDigitalTwinDescriptor;
        this.secondZoneDigitalTwinDescriptor = secondZoneDigitalTwinDescriptor;
        this.attributes = attributes;
    }

    public ConduitDigitalTwinDescriptor(String id, String type, boolean isRunning, String conduitId,
                                        ZoneDigitalTwinDescriptor firstZoneDigitalTwinDescriptor,
                                        ZoneDigitalTwinDescriptor secondZoneDigitalTwinDescriptor,
                                        Map<String, ConduitConfigurationParameter> attributes) {
        super(id, type, isRunning);
        this.conduitId = conduitId;
        this.firstZoneDigitalTwinDescriptor = firstZoneDigitalTwinDescriptor;
        this.secondZoneDigitalTwinDescriptor = secondZoneDigitalTwinDescriptor;
        this.attributes = attributes;
    }

    public String getConduitId() {
        return conduitId;
    }

    public void setConduitId(String conduitId) {
        this.conduitId = conduitId;
    }

    public ZoneDigitalTwinDescriptor getFirstZoneDigitalTwinDescriptor() {
        return firstZoneDigitalTwinDescriptor;
    }

    public void setFirstZoneDigitalTwinDescriptor(ZoneDigitalTwinDescriptor firstZoneDigitalTwinDescriptor) {
        this.firstZoneDigitalTwinDescriptor = firstZoneDigitalTwinDescriptor;
    }

    public ZoneDigitalTwinDescriptor getSecondZoneDigitalTwinDescriptor() {
        return secondZoneDigitalTwinDescriptor;
    }

    public void setSecondZoneDigitalTwinDescriptor(ZoneDigitalTwinDescriptor secondZoneDigitalTwinDescriptor) {
        this.secondZoneDigitalTwinDescriptor = secondZoneDigitalTwinDescriptor;
    }

    public Map<String, ConduitConfigurationParameter> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, ConduitConfigurationParameter> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ConduitDigitalTwinDescriptor{");
        sb.append("conduitId='").append(conduitId).append('\'');
        sb.append(", firstZoneDigitalTwinDescriptor=").append(firstZoneDigitalTwinDescriptor);
        sb.append(", secondZoneDigitalTwinDescriptor=").append(secondZoneDigitalTwinDescriptor);
        sb.append(", attributes=").append(attributes);
        sb.append('}');
        return sb.toString();
    }
}
