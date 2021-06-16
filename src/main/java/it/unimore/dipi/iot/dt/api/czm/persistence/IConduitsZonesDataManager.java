package it.unimore.dipi.iot.dt.api.czm.persistence;

import it.unimore.dipi.iot.dt.api.czm.exception.ConduitsZonesDataManagerConflict;
import it.unimore.dipi.iot.dt.api.czm.exception.ConduitsZonesDataManagerException;
import it.unimore.dipi.iot.dt.api.czm.model.ConduitDigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.czm.model.EdgeNodeDescriptor;
import it.unimore.dipi.iot.dt.api.czm.model.ZoneDigitalTwinDescriptor;

import java.util.List;
import java.util.Optional;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:44
 */
public interface IConduitsZonesDataManager {

    //Zones
    public List<ZoneDigitalTwinDescriptor> getZoneDigitalTwinList() throws ConduitsZonesDataManagerException;

    public Optional<ZoneDigitalTwinDescriptor> getZoneDigitalTwinById(String zoneId) throws ConduitsZonesDataManagerException;

    public ZoneDigitalTwinDescriptor createNewZoneDigitalTwin(ZoneDigitalTwinDescriptor zoneDigitalTwinDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict;

    public ZoneDigitalTwinDescriptor updateZoneDigitalTwin(ZoneDigitalTwinDescriptor zoneDigitalTwinDescriptor) throws ConduitsZonesDataManagerException;

    public ZoneDigitalTwinDescriptor deleteZoneDigitalTwin(String zoneId) throws ConduitsZonesDataManagerException;

    //Conduits
    public List<ConduitDigitalTwinDescriptor> getConduitDigitalTwinList() throws ConduitsZonesDataManagerException;

    public Optional<ConduitDigitalTwinDescriptor> getConduitDigitalTwinById(String conduitId) throws ConduitsZonesDataManagerException;

    public ConduitDigitalTwinDescriptor createNewConduitDigitalTwin(ConduitDigitalTwinDescriptor conduitDigitalTwinDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict;

    public ConduitDigitalTwinDescriptor updateConduitDigitalTwin(ConduitDigitalTwinDescriptor conduitDigitalTwinDescriptor) throws ConduitsZonesDataManagerException;

    public ConduitDigitalTwinDescriptor deleteConduitDigitalTwin(String conduitId) throws ConduitsZonesDataManagerException;

    //Edge Nodes
    public List<EdgeNodeDescriptor> getEdgeNodeList() throws ConduitsZonesDataManagerException;

    public Optional<EdgeNodeDescriptor> getEdgeNodeById(String edgeNodeId) throws ConduitsZonesDataManagerException;

    public EdgeNodeDescriptor createNewEdgeNode(EdgeNodeDescriptor edgeNodeDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict;

    public EdgeNodeDescriptor updateEdgeNode(EdgeNodeDescriptor edgeNodeDescriptor) throws ConduitsZonesDataManagerException;

    public void deleteEdgeNode(String edgeNodeId) throws ConduitsZonesDataManagerException;

}
