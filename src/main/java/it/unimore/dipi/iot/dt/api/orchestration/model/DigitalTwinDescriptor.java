package it.unimore.dipi.iot.dt.api.orchestration.model;

public class DigitalTwinDescriptor {

    public static final String DEFAULT_CATEGORY = "default";

    private String id;

    private String category = DEFAULT_CATEGORY;

    private String type; //MQTT or COAP digital twin

    private boolean isRunning;

    public DigitalTwinDescriptor() {
    }

    public DigitalTwinDescriptor(String id, String type, boolean isRunning) {
        this.id = id;
        this.type = type;

        this.isRunning = isRunning;
    }

    public DigitalTwinDescriptor(String id, String category, String type, boolean isRunning) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.isRunning = isRunning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DigitalTwinDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", isRunning=").append(isRunning);
        sb.append('}');
        return sb.toString();
    }
}
