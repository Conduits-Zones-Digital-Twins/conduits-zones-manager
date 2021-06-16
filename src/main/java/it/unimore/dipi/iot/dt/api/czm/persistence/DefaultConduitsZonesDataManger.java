package it.unimore.dipi.iot.dt.api.czm.persistence;

import it.unimore.dipi.iot.dt.api.czm.exception.ConduitsZonesDataManagerConflict;
import it.unimore.dipi.iot.dt.api.czm.exception.ConduitsZonesDataManagerException;
import it.unimore.dipi.iot.dt.api.czm.model.ConduitDigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.czm.model.EdgeNodeDescriptor;
import it.unimore.dipi.iot.dt.api.czm.model.ZoneDigitalTwinDescriptor;

import java.util.*;

/**
 *
 * Demo IoT Inventory Data Manager handling all data in a local cache implemented through Maps and Lists
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:48
 */
public class DefaultConduitsZonesDataManger implements IConduitsZonesDataManager {

    private Map<String, ZoneDigitalTwinDescriptor> zoneDtMap;

    private Map<String, ConduitDigitalTwinDescriptor> conduitDtMap;

    private Map<String, EdgeNodeDescriptor> edgeNodeMap;

    public DefaultConduitsZonesDataManger() {
        this.zoneDtMap = new HashMap<>();
        this.conduitDtMap = new HashMap<>();
        this.edgeNodeMap = new HashMap<>();
    }

    //ZONES MANAGEMENT

    @Override
    public List<ZoneDigitalTwinDescriptor> getZoneDigitalTwinList() throws ConduitsZonesDataManagerException {
        return new ArrayList<>(this.zoneDtMap.values());
    }

    @Override
    public Optional<ZoneDigitalTwinDescriptor> getZoneDigitalTwinById(String zoneId) throws ConduitsZonesDataManagerException {
        return Optional.ofNullable(this.zoneDtMap.get(zoneId));
    }

    @Override
    public ZoneDigitalTwinDescriptor createNewZoneDigitalTwin(ZoneDigitalTwinDescriptor zoneDigitalTwinDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict {

        if(zoneDigitalTwinDescriptor != null && this.getZoneDigitalTwinById(zoneDigitalTwinDescriptor.getId()).isPresent())
            throw new ConduitsZonesDataManagerConflict("DigitalTwin with the same id already available!");

        this.zoneDtMap.put(zoneDigitalTwinDescriptor.getId(), zoneDigitalTwinDescriptor);
        return zoneDigitalTwinDescriptor;

    }

    @Override
    public ZoneDigitalTwinDescriptor updateZoneDigitalTwin(ZoneDigitalTwinDescriptor zoneDigitalTwinDescriptor) throws ConduitsZonesDataManagerException {
        this.zoneDtMap.put(zoneDigitalTwinDescriptor.getId(), zoneDigitalTwinDescriptor);
        return zoneDigitalTwinDescriptor;
    }

    @Override
    public ZoneDigitalTwinDescriptor deleteZoneDigitalTwin(String zoneId) throws ConduitsZonesDataManagerException {
        return this.zoneDtMap.remove(zoneId);
    }

    //CONDUITS MANAGEMENT

    @Override
    public List<ConduitDigitalTwinDescriptor> getConduitDigitalTwinList() throws ConduitsZonesDataManagerException {
        return new ArrayList<>(this.conduitDtMap.values());
    }

    @Override
    public Optional<ConduitDigitalTwinDescriptor> getConduitDigitalTwinById(String conduitId) throws ConduitsZonesDataManagerException {
        return Optional.ofNullable(this.conduitDtMap.get(conduitId));
    }

    @Override
    public ConduitDigitalTwinDescriptor createNewConduitDigitalTwin(ConduitDigitalTwinDescriptor conduitDigitalTwinDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict {
        if(conduitDigitalTwinDescriptor != null && this.getConduitDigitalTwinById(conduitDigitalTwinDescriptor.getId()).isPresent())
            throw new ConduitsZonesDataManagerException("DigitalTwin with the same id already available!");

        this.conduitDtMap.put(conduitDigitalTwinDescriptor.getId(), conduitDigitalTwinDescriptor);
        return conduitDigitalTwinDescriptor;
    }

    @Override
    public ConduitDigitalTwinDescriptor updateConduitDigitalTwin(ConduitDigitalTwinDescriptor conduitDigitalTwinDescriptor) throws ConduitsZonesDataManagerException {
        this.conduitDtMap.put(conduitDigitalTwinDescriptor.getId(), conduitDigitalTwinDescriptor);
        return conduitDigitalTwinDescriptor;
    }

    @Override
    public ConduitDigitalTwinDescriptor deleteConduitDigitalTwin(String conduitId) throws ConduitsZonesDataManagerException {
        return this.conduitDtMap.remove(conduitId);
    }

    //EDGE NODES

    @Override
    public List<EdgeNodeDescriptor> getEdgeNodeList() throws ConduitsZonesDataManagerException {
        return new ArrayList<>(this.edgeNodeMap.values());
    }

    @Override
    public Optional<EdgeNodeDescriptor> getEdgeNodeById(String edgeNodeId) throws ConduitsZonesDataManagerException {
        return Optional.ofNullable(this.edgeNodeMap.get(edgeNodeId));
    }

    @Override
    public EdgeNodeDescriptor createNewEdgeNode(EdgeNodeDescriptor edgeNodeDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict {
        if(edgeNodeDescriptor != null && this.getEdgeNodeById(edgeNodeDescriptor.getId()).isPresent())
            throw new ConduitsZonesDataManagerConflict("Edge Node with the same id already available!");

        this.edgeNodeMap.put(edgeNodeDescriptor.getId(), edgeNodeDescriptor);
        return edgeNodeDescriptor;
    }

    @Override
    public EdgeNodeDescriptor updateEdgeNode(EdgeNodeDescriptor edgeNodeDescriptor) throws ConduitsZonesDataManagerException {
        this.edgeNodeMap.put(edgeNodeDescriptor.getId(), edgeNodeDescriptor);
        return edgeNodeDescriptor;
    }

    @Override
    public void deleteEdgeNode(String edgeNodeId) throws ConduitsZonesDataManagerException {
        this.edgeNodeMap.remove(edgeNodeId);
    }
}
