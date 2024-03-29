package it.unimore.dipi.iot.dt.api.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.dipi.iot.dt.api.czm.resources.CzmEdgeNodeResource;
import it.unimore.dipi.iot.dt.api.orchestration.resources.ConfigurationResource;
import it.unimore.dipi.iot.dt.api.orchestration.resources.DigitalTwinResource;
import it.unimore.dipi.iot.dt.api.orchestration.resources.WldtWorkerConfigurationResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class AppService extends Application<AppConfig> {

    public static void main(String[] args) throws Exception{

        new AppService().run(new String[]{"server", args.length > 0 ? args[0] : "configuration.yml"});
    }

    public void run(AppConfig appConfig, Environment environment) throws Exception {

        //Add our defined resources
        environment.jersey().register(new DigitalTwinResource(appConfig));
        environment.jersey().register(new ConfigurationResource(appConfig));
        environment.jersey().register(new WldtWorkerConfigurationResource(appConfig));

        //CZM RESOURCES
        environment.jersey().register(new CzmEdgeNodeResource(appConfig));

        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }

    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {

        bootstrap.addBundle(new SwaggerBundle<AppConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfig configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

    }
}