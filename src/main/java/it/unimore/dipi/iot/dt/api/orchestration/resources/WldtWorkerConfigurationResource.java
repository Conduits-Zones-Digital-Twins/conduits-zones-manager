package it.unimore.dipi.iot.dt.api.orchestration.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.dt.api.orchestration.dto.WldtWorkerConfigurationCreationRequest;
import it.unimore.dipi.iot.dt.api.orchestration.dto.WldtWorkerConfigurationUpdateRequest;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtWorkerConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.services.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import sun.security.krb5.Config;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/digitalTwin/{dt_id}/conf/worker/")
@Api("IoT Configuration Inventory Endpoint")
public class WldtWorkerConfigurationResource {

    final protected Logger logger = LoggerFactory.getLogger(WldtWorkerConfigurationResource.class);

    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public WldtWorkerConfigurationResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get all worker resources")
    public Response getWorkers(@Context ContainerRequestContext req,
                                    @PathParam("dt_id") String digitalTwinId) {

        try {

            logger.info("Loading all stored workers ...");

            String confId = digitalTwinId.replace("dt","conf");

            List<WldtWorkerConfigurationDescriptor> workers = this.conf.getInventoryDataManager().getWorkerList(confId);

            if(workers == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Workers Not Found !")).build();

            return Response.ok(workers).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{worker_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get Worker by Id")
    public Response getWorkerById(@Context ContainerRequestContext req,
                                       @PathParam("dt_id") String digitalTwinId,
                                       @PathParam("worker_id") String workerId) {

        try {

            logger.info("Loading Digital Twin Info for id: {}", digitalTwinId);

            String confId = digitalTwinId.replace("dt","conf");

            //Check the request
            if(workerId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Worker Id Provided !")).build();

            WldtWorkerConfigurationDescriptor worker = this.conf.getInventoryDataManager().getWorkerById(confId, workerId);

            if(worker == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Worker Not Found !")).build();

            return Response.ok(worker).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @POST
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Create a new Worker")
    public Response createDigitalTwin(@Context ContainerRequestContext req,
                                      @Context UriInfo uriInfo,
                                      @PathParam("dt_id") String digitalTwinId,
                                      WldtWorkerConfigurationCreationRequest wldtWorkerConfigurationCreationRequest) {

        try {

            String confId = digitalTwinId.replace("dt","conf");
            logger.info("Incoming Worker Creation Request: {}", wldtWorkerConfigurationCreationRequest);

            //Check the request
            if(wldtWorkerConfigurationCreationRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload")).build();

            WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor = (WldtWorkerConfigurationDescriptor) wldtWorkerConfigurationCreationRequest;
            //Reset Id to null. The ID can not be provided by the client. It is generated by the server
            //userDescriptor.setInternalId(null);
            wldtWorkerConfigurationDescriptor = this.conf.getInventoryDataManager().createNewWorker(confId, wldtWorkerConfigurationDescriptor);

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),wldtWorkerConfigurationDescriptor.getWorkerId()))).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/{worker_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Update an existing Worker")
    public Response updateWorker(@Context ContainerRequestContext req,
                                      @Context UriInfo uriInfo,
                                      @PathParam("dt_id") String digitalTwinId,
                                      @PathParam("worker_id") String workerId,
                                      WldtWorkerConfigurationUpdateRequest wldtWorkerConfigurationUpdateRequest) {

        try {

            logger.info("Incoming Worker ({}) Update Request: {}", workerId, wldtWorkerConfigurationUpdateRequest);
            String confId = digitalTwinId.replace("dt", "conf");

            //Check if the request is valid
            if(wldtWorkerConfigurationUpdateRequest == null || !wldtWorkerConfigurationUpdateRequest.getWorkerId().equals(workerId))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Worker Id")).build();

            WldtWorkerConfigurationDescriptor newWldtWorkerConfigurationDescriptor = (WldtWorkerConfigurationDescriptor) wldtWorkerConfigurationUpdateRequest;
            WldtWorkerConfigurationDescriptor oldWldtWorkerConfigurationDescriptor = this.conf.getInventoryDataManager().getWorkerById(confId, workerId);
            this.conf.getInventoryDataManager().updateWorker(confId, oldWldtWorkerConfigurationDescriptor, newWldtWorkerConfigurationDescriptor);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/{worker_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Delete a Single Worker")
    public Response deleteWorker(@Context ContainerRequestContext req,
                                      @PathParam("dt_id") String digitalTwinId,
                                      @PathParam("worker_id") String workerId) {

        try {

            String confId = digitalTwinId.replace("dt","conf");
            logger.info("Deleting Worker with id: {}", workerId);

            //Check the request
            if(workerId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Worker Id Provided !")).build();

            //Check if the worker is available or not
            if(!this.conf.getInventoryDataManager().getConfiguration(confId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"DigitalTwin Not Found !")).build();

            //Delete the worker
            this.conf.getInventoryDataManager().deleteWorker(confId, this.conf.getInventoryDataManager().getWorkerById(confId, workerId));

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }

    }


}
