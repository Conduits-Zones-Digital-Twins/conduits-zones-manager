package it.unimore.dipi.iot.dt.api.orchestration.exception;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 12:59
 */
public class DigitalTwinExecutionRequestException extends Exception {

    public DigitalTwinExecutionRequestException(String errorMessage){
        super(errorMessage);
    }

}
