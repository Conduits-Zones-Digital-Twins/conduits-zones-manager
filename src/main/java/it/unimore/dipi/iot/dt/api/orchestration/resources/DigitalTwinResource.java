package it.unimore.dipi.iot.dt.api.orchestration.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.dt.api.orchestration.dto.DigitalTwinCreationRequest;
import it.unimore.dipi.iot.dt.api.orchestration.dto.DigitalTwinUpdateRequest;
import it.unimore.dipi.iot.dt.api.orchestration.exception.DigitalTwinExecutionRequestException;
import it.unimore.dipi.iot.dt.api.orchestration.model.DigitalTwinDescriptor;
import it.unimore.dipi.iot.dt.api.services.AppConfig;
import it.unimore.dipi.iot.dt.utils.CommandLineException;
import it.unimore.dipi.iot.dt.utils.CommandLineExecutor;
import it.unimore.dipi.iot.dt.utils.CommandLineResult;
import it.unimore.dipi.iot.dt.utils.LinuxCliExecutor;
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
import java.util.Optional;

@Path("/digitalTwin")
@Api("IoT DigitalTwin Inventory Endpoint")
public class DigitalTwinResource {

    final protected Logger logger = LoggerFactory.getLogger(DigitalTwinResource.class);

    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public DigitalTwinResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get all digital twin resources")
    public Response getDigitalTwins(@Context ContainerRequestContext req) {

        try {

            logger.info("Loading all stored digital twin devices ...");

            List<DigitalTwinDescriptor> digitalTwinList = this.conf.getInventoryDataManager().getDigitalTwinList();

            if(digitalTwinList == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Devices Not Found !")).build();

            return Response.ok(digitalTwinList).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{dt_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get Digital Twin by Id")
    public Response getDigitalTwinById(@Context ContainerRequestContext req,
                                @PathParam("dt_id") String digitalTwinId) {

        try {

            logger.info("Loading Digital Twin Info for id: {}", digitalTwinId);

            //Check the request
            if(digitalTwinId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid DigitalTwin Id Provided !")).build();

            Optional<DigitalTwinDescriptor> digitalTwinDescriptor = this.conf.getInventoryDataManager().getDigitalTwinById(digitalTwinId);

            if(!digitalTwinDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"DigitalTwin Not Found !")).build();

            return Response.ok(digitalTwinDescriptor.get()).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @POST
    @Path("/{dt_id}/start")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Start the target DT")
    public Response startTargetDigitalTwin(@Context ContainerRequestContext req,
                                       @PathParam("dt_id") String digitalTwinId) {

        try {

            logger.info("Starting the target Digital Twin with id: {}", digitalTwinId);

            //Check the request
            if(digitalTwinId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid DigitalTwin Id Provided !")).build();

            Optional<DigitalTwinDescriptor> optionalDigitalTwinDescriptor = this.conf.getInventoryDataManager().getDigitalTwinById(digitalTwinId);

            if(!optionalDigitalTwinDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"DigitalTwin Not Found !")).build();
            else
                startDigitalTwinByType(optionalDigitalTwinDescriptor.get().getId(), optionalDigitalTwinDescriptor.get().getType());

            return Response.noContent().build();

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
    @ApiOperation(value="Create a new DigitalTwin")
    public Response createDigitalTwin(@Context ContainerRequestContext req,
                               @Context UriInfo uriInfo,
                               DigitalTwinCreationRequest digitalTwinCreationRequest) {

        try {

            logger.info("Incoming User Creation Request: {}", digitalTwinCreationRequest);

            //Check the request
            if(digitalTwinCreationRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload")).build();

            DigitalTwinDescriptor digitalTwinDescriptor = (DigitalTwinDescriptor) digitalTwinCreationRequest;
            //Reset Id to null. The ID can not be provided by the client. It is generated by the server
            //userDescriptor.setInternalId(null);
            digitalTwinDescriptor = this.conf.getInventoryDataManager().createNewDigitalTwin(digitalTwinDescriptor);

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),digitalTwinDescriptor.getId()))).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/{dt_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Update an existing DigitalTwin")
    public Response updateDigitalTwin(@Context ContainerRequestContext req,
                                   @Context UriInfo uriInfo,
                                   @PathParam("dt_id") String digitalTwinId,
                                   DigitalTwinUpdateRequest digitalTwinUpdateRequest) {

        try {

            logger.info("Incoming DigitalTwin ({}) Update Request: {}", digitalTwinId, digitalTwinUpdateRequest);

            //Check if the request is valid
            if(digitalTwinUpdateRequest == null || !digitalTwinUpdateRequest.getId().equals(digitalTwinId))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check DigitalTwin Id")).build();

            //Check if the device is available and correctly registered otherwise a 404 response will be sent to the client
            if(!this.conf.getInventoryDataManager().getDigitalTwinById(digitalTwinId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"DigitalTwin not found !")).build();

            DigitalTwinDescriptor digitalTwinDescriptor = (DigitalTwinDescriptor) digitalTwinUpdateRequest;
            this.conf.getInventoryDataManager().updateDigitalTwin(digitalTwinDescriptor);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/{dt_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Delete a Single DigitalTwin")
    public Response deleteDigitalTwin(@Context ContainerRequestContext req,
                                 @PathParam("dt_id") String digitalTwinId) {

        try {

            logger.info("Deleting DigitalTwin with id: {}", digitalTwinId);

            //Check the request
            if(digitalTwinId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid DigitalTwin Id Provided !")).build();

            //Check if the device is available or not
            if(!this.conf.getInventoryDataManager().getDigitalTwinById(digitalTwinId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"DigitalTwin Not Found !")).build();

            //Delete the location
            this.conf.getInventoryDataManager().deleteDigitalTwin(digitalTwinId);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }

    }

    private void startDigitalTwinByType(String dtId, String type) throws DigitalTwinExecutionRequestException, CommandLineException {

        String containerType = null;

        if(type.equals("MQTT"))
            containerType = "registry.gitlab.com/piconem-university/projects/dt-conduits-zones-iiot/wldt-mqtt-example:0.1";
        else if(type.equals("CDT"))
            containerType = "-p 8080:8080 registry.gitlab.com/piconem-university/projects/dt-conduits-zones-iiot/wldt-http-cdt:0.1";
        else if(type.equals("ZDT"))
            containerType = "-p 9090:9090 registry.gitlab.com/piconem-university/projects/dt-conduits-zones-iiot/wldt-http-zdt:0.1";
        else if(type.equals("NDT"))
            containerType = "-p 6060:6060 registry.gitlab.com/piconem-university/projects/dt-conduits-zones-iiot/wldt-http-ndt:0.1";
        else
            throw new DigitalTwinExecutionRequestException("Digital Twin Type not found !");

        String dtName = String.format("czm_%s_dt", dtId);

        String startContainerCommand = String.format("docker run --name=%s -m 128m -d %s", dtName, containerType);

        CommandLineExecutor commandLineExecutor = new LinuxCliExecutor();
        Optional<CommandLineResult> optionalResult = commandLineExecutor.executeCommand(startContainerCommand);

        optionalResult.ifPresent(commandLineResult -> logger.info("Command Result: {}", commandLineResult));

        if(!optionalResult.isPresent())
            throw new DigitalTwinExecutionRequestException("Error Executing DT ! Result Null !");
        else {
            CommandLineResult commandLineResult = optionalResult.get();
            if(commandLineResult.getExitValue() != 0 || commandLineResult.getErrorString().length() > 0)
                throw new DigitalTwinExecutionRequestException(String.format("Error Executing DT ! Msd: %s", commandLineResult.getErrorString()));
        }
    }

}
