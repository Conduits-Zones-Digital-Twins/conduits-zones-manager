package it.unimore.dipi.iot.dt.api.czm.persistence.mysql;

import it.unimore.dipi.iot.dt.api.czm.exception.ConduitsZonesDataManagerConflict;
import it.unimore.dipi.iot.dt.api.czm.exception.ConduitsZonesDataManagerException;
import it.unimore.dipi.iot.dt.api.czm.model.ConduitDigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.czm.model.EdgeNodeDescriptor;
import it.unimore.dipi.iot.dt.api.czm.model.ZoneDigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.czm.persistence.IConduitsZonesDataManager;
import it.unimore.dipi.iot.dt.utils.JdbcDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Demo IoT Inventory Data Manager handling all data in a local cache implemented through Maps and Lists
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:48
 */
public class MySqlConduitsZonesDataManger implements IConduitsZonesDataManager {

    final private Logger logger = LoggerFactory.getLogger(MySqlConduitsZonesDataManger.class);

    private Map<String, ZoneDigitalTwinDescriptor> zoneDtMap;

    private Map<String, ConduitDigitalTwinDescriptor> conduitDtMap;

    private Map<String, EdgeNodeDescriptor> edgeNodeMap;

    private JdbcDataAccess jdbcDataAccess;

    public MySqlConduitsZonesDataManger(JdbcDataAccess jdbcDataAccess) {
        this.jdbcDataAccess = jdbcDataAccess;
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

        return this.jdbcDataAccess.onDBConnection(()->{
            List<EdgeNodeModel> modelList = EdgeNodeModel.findAll();

            return modelList.stream()
                    .map(m -> new EdgeNodeDescriptor(m.getString(EdgeNodeModel.COLUMN_KEY_ID),
                            m.getString(EdgeNodeModel.COLUMN_KEY_TYPE),
                            m.getString(EdgeNodeModel.COLUMN_KEY_IP_ADDRESS),
                            m.getString(EdgeNodeModel.COLUMN_KEY_BASE_URL)))
                    .collect(Collectors.toList());
        });
    }

    @Override
    public Optional<EdgeNodeDescriptor> getEdgeNodeById(String edgeNodeId) throws ConduitsZonesDataManagerException {

        logger.info("Retrieving EdgeNode with Id: {}", edgeNodeId);

        if(edgeNodeId == null || edgeNodeId.length() == 0)
            throw new ConduitsZonesDataManagerException("Wrong or missing edgeNodeId !");

        try{
            return this.jdbcDataAccess.onDBConnection(()->{
                try{
                    List<EdgeNodeModel> modelList = EdgeNodeModel.where("edge_node_id = ?", edgeNodeId);

                    if(modelList == null || modelList.size() == 0)
                        return Optional.empty();

                    return modelList.stream()
                            .map(m -> new EdgeNodeDescriptor(m.getString(EdgeNodeModel.COLUMN_KEY_ID),
                                    m.getString(EdgeNodeModel.COLUMN_KEY_TYPE),
                                    m.getString(EdgeNodeModel.COLUMN_KEY_IP_ADDRESS),
                                    m.getString(EdgeNodeModel.COLUMN_KEY_BASE_URL)))
                            .findFirst();
                }catch (Exception e){
                    logger.error("Error loading EdgeNodeById ! Msg: {}", e.getMessage());
                    return Optional.empty();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public EdgeNodeDescriptor createNewEdgeNode(EdgeNodeDescriptor edgeNodeDescriptor) throws ConduitsZonesDataManagerException, ConduitsZonesDataManagerConflict {

        //Check Resource Conflict
        if(edgeNodeDescriptor != null && edgeNodeDescriptor.getId() != null && this.getEdgeNodeById(edgeNodeDescriptor.getId()).isPresent())
            throw new ConduitsZonesDataManagerConflict("Edge Node with the same id already available!");

        //Validate Input
        //if(edgeNodeDescriptor.getId() == null || edgeNodeDescriptor.getId().length() == 0)
            //throw new ConduitsZonesDataManagerException("Missing or Wrong Parameters ! Check the incoming resource object !");

        if(edgeNodeDescriptor.getId() == null || edgeNodeDescriptor.getId().length() == 0)
            edgeNodeDescriptor.setId(UUID.randomUUID().toString());

        boolean insertResult = false;

        try{
            insertResult = this.jdbcDataAccess.onDBConnection(()->{
                try{

                    EdgeNodeModel model = new EdgeNodeModel();
                    model.setString(EdgeNodeModel.COLUMN_KEY_ID, edgeNodeDescriptor.getId());
                    model.setString(EdgeNodeModel.COLUMN_KEY_TYPE, edgeNodeDescriptor.getType());
                    model.setString(EdgeNodeModel.COLUMN_KEY_IP_ADDRESS, edgeNodeDescriptor.getIpAddress());
                    model.setString(EdgeNodeModel.COLUMN_KEY_BASE_URL, edgeNodeDescriptor.getDtManagerServiceBaseUrl());
                    return model.insert();

                }catch (Exception e){
                    logger.error("ActiveJdbc Exception ! Msg: {}", e.getMessage());
                    throw new ConduitsZonesDataManagerException(e.getMessage());
                }
            });
        }catch (Exception e){
            throw new ConduitsZonesDataManagerException(e.getMessage());
        }

        if(!insertResult)
            throw new ConduitsZonesDataManagerException("Error creating the new resource !");
        else
            return edgeNodeDescriptor;
    }

    @Override
    public EdgeNodeDescriptor updateEdgeNode(EdgeNodeDescriptor edgeNodeDescriptor) throws ConduitsZonesDataManagerException {

        //Validate Input
        if(edgeNodeDescriptor == null || edgeNodeDescriptor.getId() == null || edgeNodeDescriptor.getId().length() == 0)
            throw new ConduitsZonesDataManagerException("Missing or Wrong Parameters ! Check the incoming resource object !");

        int resultCount = -1;

        try{
            resultCount = this.jdbcDataAccess.onDBConnection(()->{
                try{

                    return EdgeNodeModel.update(
                            String.format("%s = ?, " +
                                    "%s = ?, " +
                                    "%s = ?",
                            EdgeNodeModel.COLUMN_KEY_TYPE,
                            EdgeNodeModel.COLUMN_KEY_IP_ADDRESS,
                            EdgeNodeModel.COLUMN_KEY_BASE_URL),
                            String.format("%s = ?", EdgeNodeModel.COLUMN_KEY_ID),
                            edgeNodeDescriptor.getType(),
                            edgeNodeDescriptor.getIpAddress(),
                            edgeNodeDescriptor.getDtManagerServiceBaseUrl(),
                            edgeNodeDescriptor.getId());

                }catch (Exception e){
                    logger.error("ActiveJdbc Exception ! Msg: {}", e.getMessage());
                    throw new ConduitsZonesDataManagerException(e.getMessage());
                }
            });
        }catch (Exception e){
            throw new ConduitsZonesDataManagerException(e.getMessage());
        }

        if(resultCount <= 0)
            throw new ConduitsZonesDataManagerException("Error updating the new resource !");
        else
            return edgeNodeDescriptor;

    }

    @Override
    public void deleteEdgeNode(String edgeNodeId) throws ConduitsZonesDataManagerException {

        //Validate Input
        if(edgeNodeId == null || edgeNodeId.length() == 0)
            throw new ConduitsZonesDataManagerException(String.format("Missing or Wrong Parameters ! Provided ResourceId: %s", edgeNodeId));

        int resultCount = -1;

        try{
            resultCount = this.jdbcDataAccess.onDBConnection(()->{
                try{

                    return EdgeNodeModel.delete(
                            String.format("%s = ?", EdgeNodeModel.COLUMN_KEY_ID),
                            edgeNodeId);
                }catch (Exception e){
                    logger.error("ActiveJdbc Exception ! Msg: {}", e.getMessage());
                    throw new ConduitsZonesDataManagerException(e.getMessage());
                }
            });
        }catch (Exception e){
            throw new ConduitsZonesDataManagerException(e.getMessage());
        }

        if(resultCount <= 0)
            throw new ConduitsZonesDataManagerException("Error deleting the resource !");
    }
}
