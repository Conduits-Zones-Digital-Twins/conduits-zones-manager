package it.unimore.dipi.iot.dt.utils;

import java.util.Optional;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project openness-connector
 * @created 01/10/2020 - 09:04
 */
public interface CommandLineExecutor {

    public Optional<CommandLineResult> executeCommand(String command) throws CommandLineException;

}
