package it.unimore.dipi.iot.dt.api.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.dipi.iot.dt.api.czm.persistence.IConduitsZonesDataManager;
import it.unimore.dipi.iot.dt.api.czm.persistence.mysql.MySqlConduitsZonesDataManger;
import it.unimore.dipi.iot.dt.api.orchestration.persistence.DefaultIotInventoryDataManger;
import it.unimore.dipi.iot.dt.api.orchestration.persistence.IIoInventoryDataManager;
import it.unimore.dipi.iot.dt.utils.JdbcDataAccess;

public class AppConfig extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty("mysqlUrl")
    private String mysqlUrl;

    @JsonProperty("mysqlUsername")
    private String mysqlUsername;

    @JsonProperty("mysqlPassword")
    private String mysqlPassword;

    private IIoInventoryDataManager inventoryDataManager = null;

    private IConduitsZonesDataManager conduitsZonesDataManager = null;

    private JdbcDataAccess jdbcDataAccess = null;

    public IIoInventoryDataManager getInventoryDataManager(){

        if(this.inventoryDataManager == null)
            this.inventoryDataManager = new DefaultIotInventoryDataManger();

        return this.inventoryDataManager;
    }

    public IConduitsZonesDataManager getConduitsZonesDataManager(){

        if(this.conduitsZonesDataManager == null)
            this.conduitsZonesDataManager = new MySqlConduitsZonesDataManger(getJdbcDataAccess());

        return this.conduitsZonesDataManager;
    }

    public JdbcDataAccess getJdbcDataAccess(){

        if (jdbcDataAccess != null)
            return jdbcDataAccess;

        jdbcDataAccess = new JdbcDataAccess(mysqlUrl, mysqlUsername, mysqlPassword);

        return jdbcDataAccess;
    }

    public String getMysqlUrl() {
        return mysqlUrl;
    }

    public void setMysqlUrl(String mysqlUrl) {
        this.mysqlUrl = mysqlUrl;
    }

    public String getMysqlUsername() {
        return mysqlUsername;
    }

    public void setMysqlUsername(String mysqlUsername) {
        this.mysqlUsername = mysqlUsername;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }
}