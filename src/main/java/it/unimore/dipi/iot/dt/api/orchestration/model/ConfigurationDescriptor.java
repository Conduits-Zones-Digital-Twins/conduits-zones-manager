package it.unimore.dipi.iot.dt.api.orchestration.model;

public class ConfigurationDescriptor{

    private String confId;
    private String confType;
    private WldtConfigurationDescriptor confValue;

    public ConfigurationDescriptor() {
    }

    public ConfigurationDescriptor(String confId, String confType, WldtConfigurationDescriptor confValue) {
        this.confId = confId;
        this.confType = confType;
        this.confValue = confValue;
    }

    public String getConfId() {
        return confId;
    }

    public void setConfId(String confId) {
        this.confId = confId;
    }

    public String getConfType() {
        return confType;
    }

    public void setConfType(String confType) {
        this.confType = confType;
    }

    public WldtConfigurationDescriptor getConfValue() {
        return confValue;
    }

    public void setConfValue(WldtConfigurationDescriptor confValue) {
        this.confValue = confValue;
    }

    @Override
    public String toString() {
        return "ConfigurationDescriptor{" +
                "confId='" + confId + '\'' +
                ", confType='" + confType + '\'' +
                ", wldtConfigurationDescriptor=" + confValue +
                '}';
    }


}
