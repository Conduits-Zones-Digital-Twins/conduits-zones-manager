package it.unimore.dipi.iot.dt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project openness-connector
 * @created 30/09/2020 - 21:34
 */
public class LinuxCliExecutor implements CommandLineExecutor {

    private static final Logger logger = LoggerFactory.getLogger(LinuxCliExecutor.class);

    public Optional<CommandLineResult> executeCommand(String command) throws CommandLineException {

        try{

            logger.info("Executing command: {}", command);

            CommandLineResult commandLineResult = new CommandLineResult();
            commandLineResult.setExecutedComamnd(command);

            long startTimestamp = System.currentTimeMillis();

            Runtime runtime = Runtime.getRuntime();
            Process pr = runtime.exec(command);

            pr.waitFor();

            long endTimestamp = System.currentTimeMillis();
            long executionTime = endTimestamp - startTimestamp;

            commandLineResult.setExecutionTime(executionTime);

            BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            String line = "";
            StringBuffer stringReaderBuffer = new StringBuffer();

            while((line = inputBufferedReader.readLine()) != null)
                stringReaderBuffer.append(line);

            commandLineResult.setOutputString(stringReaderBuffer.toString());
            stringReaderBuffer.delete(0, stringReaderBuffer.length());

            while((line = errorBufferedReader.readLine()) != null)
                stringReaderBuffer.append(line);

            commandLineResult.setErrorString(stringReaderBuffer.toString());

            return Optional.of(commandLineResult);

        }catch (Exception e){
            throw new CommandLineException(String.format("Error Executing Command: %s Error: %s", command, e.getLocalizedMessage()));
        }

    }

    public static void main(String[] args) {

        try{
            //String command = "keytool -list -keystore example.client.chain.p12 -storepass changeit";

            String startContainerCommand = "docker run --name=wl-digital-twin -m 128m -d registry.gitlab.com/piconem-university/projects/dt-conduits-zones-iiot/wldt-mqtt-example:0.1";
            String stopContainerCommand = "docker stop wl-digital-twin";
            String removeContainerCommand = "docker rm wl-digital-twin";

            CommandLineExecutor commandLineExecutor = new LinuxCliExecutor();
            Optional<CommandLineResult> optionalResult = commandLineExecutor.executeCommand(startContainerCommand);
            optionalResult.ifPresent(commandLineResult -> logger.info("Command Result: {}", commandLineResult));

            Thread.sleep(30000);

            optionalResult = commandLineExecutor.executeCommand(stopContainerCommand);
            optionalResult.ifPresent(commandLineResult -> logger.info("Command Result: {}", commandLineResult));

            optionalResult = commandLineExecutor.executeCommand(removeContainerCommand);
            optionalResult.ifPresent(commandLineResult -> logger.info("Command Result: {}", commandLineResult));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
