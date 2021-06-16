package it.unimore.dipi.iot.dt.api.czm.model;

import java.util.Objects;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project conduits-zones-manager
 * @created 04/06/2021 - 12:18
 */
public class ConduitConfigurationParameter {

    private String key;

    private Object value;

    private boolean mandatory = false;

    public ConduitConfigurationParameter() {
    }

    public ConduitConfigurationParameter(String key, Object value, boolean mandatory) {
        this.key = key;
        this.value = value;
        this.mandatory = mandatory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConduitConfigurationParameter that = (ConduitConfigurationParameter) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
