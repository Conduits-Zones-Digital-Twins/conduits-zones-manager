package it.unimore.dipi.iot.dt.api.czm.model;

import it.unimore.dipi.iot.dt.api.orchestration.model.DigitalTwinDescriptor;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project conduits-zones-manager
 * @created 04/06/2021 - 12:03
 */
public class AssetDigitalTwinDescriptor extends DigitalTwinDescriptor {

    public AssetDigitalTwinDescriptor() {
    }

    public AssetDigitalTwinDescriptor(String id, String type, boolean isRunning) {
        super(id, type, isRunning);
    }
}
