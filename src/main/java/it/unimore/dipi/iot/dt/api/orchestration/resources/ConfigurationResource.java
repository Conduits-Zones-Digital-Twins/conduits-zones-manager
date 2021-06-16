package it.unimore.dipi.iot.dt.api.orchestration.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.dipi.iot.dt.api.orchestration.dto.ConfigurationCreationRequest;
import it.unimore.dipi.iot.dt.api.orchestration.dto.ConfigurationUpdateRequest;
import it.unimore.dipi.iot.dt.api.orchestration.model.ConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.services.AppConfig;
import it.unimore.dipi.iot.dt.utils.GenerateConfigurationFile;
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
import java.util.Optional;

@Path("/digitalTwin/{dt_id}/conf")
@Api("IoT Configuration Inventory Endpoint")

public class ConfigurationResource {

    final protected Logger logger = LoggerFactory.getLogger(ConfigurationResource.class);

    public static class MissingKeyException extends Exception{}
    private GenerateConfigurationFile generateConfigurationFile;
    final AppConfig conf;

    public ConfigurationResource(AppConfig conf) {
        this.conf = conf;
        this.generateConfigurationFile = new GenerateConfigurationFile();
    }

    @GET
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get configuration resource")
    public Response getConfiguration(@Context ContainerRequestContext req,
                                     @PathParam("dt_id") String digitalTwinId) {

        String confId = digitalTwinId.replace("dt", "conf");

        try {

            logger.info("Loading Configuration for id: {}", confId);

            //Check the request
            if(digitalTwinId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Configuration Id Provided !")).build();

            Optional<ConfigurationDescriptor> configurationDescriptor = this.conf.getInventoryDataManager().getConfiguration(confId);

            if(!configurationDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Configuration Not Found !")).build();

            return Response.ok(configurationDescriptor.get()).build();

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
    @ApiOperation(value="Create a new Configuration")
    public Response createConfiguration(@Context ContainerRequestContext req,
                                        @Context UriInfo uriInfo,
                                        ConfigurationCreationRequest configurationCreationRequest) {

        try {

            logger.info("Incoming Configuration Creation Request: {}", configurationCreationRequest);

            //Check the request
            if(configurationCreationRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload")).build();

            ConfigurationDescriptor configurationDescriptor = (ConfigurationDescriptor) configurationCreationRequest;

            configurationDescriptor = this.conf.getInventoryDataManager().createNewConfiguration(configurationDescriptor);
            generateConfigurationFile.generateFileForEngine(configurationDescriptor.getConfValue().getEngine());
            generateConfigurationFile.generateFileForWorkers(configurationDescriptor.getConfValue().getWorkers());

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),configurationDescriptor.getConfId()))).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }


    @PUT
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Update an existing Configuration")
    public Response updateConfiguration(@Context ContainerRequestContext req,
                                        @Context UriInfo uriInfo,
                                        @PathParam("dt_id") String digitalTwinId,
                                        ConfigurationUpdateRequest configurationUpdateRequest) {

        try {

            logger.info("Incoming DigitalTwin ({}) Update Request: {}", digitalTwinId, configurationUpdateRequest);

            String confId = digitalTwinId.replace("dt","conf");

            //Check if the request is valid
            if(configurationUpdateRequest == null || !configurationUpdateRequest.getConfId().equals(confId))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Configuration Id")).build();

            //Check if the configuration is available and correctly registered otherwise a 404 response will be sent to the client
            if(!this.conf.getInventoryDataManager().getConfiguration(confId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"DigitalTwin not found !")).build();

            ConfigurationDescriptor configurationDescriptor = (ConfigurationDescriptor) configurationUpdateRequest;
            this.conf.getInventoryDataManager().updateConfiguration(configurationDescriptor);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Delete the Configuration")
    public Response deleteConfiguration(@Context ContainerRequestContext req,
                                        @PathParam("dt_id") String digitalTwinId) {

        try {

            logger.info("Deleting Configuration od DigitalTwin with id: {}", digitalTwinId);
            String confId = digitalTwinId.replace("dt","conf");

            //Check the request
            if(confId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Configuration Id Provided !")).build();

            //Check if the device is available or not
            if(!this.conf.getInventoryDataManager().getConfiguration(confId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Configuration Not Found !")).build();

            //Delete the location
            this.conf.getInventoryDataManager().deleteConfiguration(confId);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }

    }

}
