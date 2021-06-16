package it.unimore.dipi.iot.dt.api.czm.model;

import it.unimore.dipi.iot.dt.api.orchestration.model.DigitalTwinDescriptor;

import java.util.List;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project conduits-zones-manager
 * @created 04/06/2021 - 12:01
 */
public class ZoneDigitalTwinDescriptor extends DigitalTwinDescriptor {

    private String zoneId;

    //List of the AssetDigitalTwin associated to the target Zone
    private List<AssetDigitalTwinDescriptor> assetDigitalTwinList;

    private List<NetworkDigitalTwinDescriptor> borderRouterDigitalTwinList;

    public ZoneDigitalTwinDescriptor(String zoneId, List<AssetDigitalTwinDescriptor> assetDigitalTwinList,
                                     List<NetworkDigitalTwinDescriptor> borderRouterDigitalTwinList) {
        this.zoneId = zoneId;
        this.assetDigitalTwinList = assetDigitalTwinList;
        this.borderRouterDigitalTwinList = borderRouterDigitalTwinList;
    }

    public ZoneDigitalTwinDescriptor(String id, String type, boolean isRunning, String zoneId,
                                     List<AssetDigitalTwinDescriptor> assetDigitalTwinList,
                                     List<NetworkDigitalTwinDescriptor> borderRouterDigitalTwinList) {
        super(id, type, isRunning);
        this.zoneId = zoneId;
        this.assetDigitalTwinList = assetDigitalTwinList;
        this.borderRouterDigitalTwinList = borderRouterDigitalTwinList;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public List<AssetDigitalTwinDescriptor> getAssetDigitalTwinList() {
        return assetDigitalTwinList;
    }

    public void setAssetDigitalTwinList(List<AssetDigitalTwinDescriptor> assetDigitalTwinList) {
        this.assetDigitalTwinList = assetDigitalTwinList;
    }

    public List<NetworkDigitalTwinDescriptor> getBorderRouterDigitalTwinList() {
        return borderRouterDigitalTwinList;
    }

    public void setBorderRouterDigitalTwinList(List<NetworkDigitalTwinDescriptor> borderRouterDigitalTwinList) {
        this.borderRouterDigitalTwinList = borderRouterDigitalTwinList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ZoneDigitalTwinDescriptor{");
        sb.append("zoneId='").append(zoneId).append('\'');
        sb.append(", assetDigitalTwinList=").append(assetDigitalTwinList);
        sb.append(", borderRouterDigitalTwinList=").append(borderRouterDigitalTwinList);
        sb.append('}');
        return sb.toString();
    }
}
