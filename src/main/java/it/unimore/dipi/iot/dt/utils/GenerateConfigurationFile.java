package it.unimore.dipi.iot.dt.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtEngineConfigurationDescriptor;
import it.unimore.dipi.iot.dt.api.orchestration.model.WldtWorkerConfigurationDescriptor;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class GenerateConfigurationFile {

    private void writeOnFile(String file_name, Object o) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(o);
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNode).replace("  ", "").replace("attributes:", "").replace("---","");
        FileWriter fileWriter = new FileWriter(file_name);
        fileWriter.write(jsonAsYaml);
        fileWriter.close();

        //arrangement of the format
        File inputFile = new File(file_name);
        File tempFile = new File("conf-test/myTempFile.txt");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.startsWith("workerId") || trimmedLine.startsWith("workerType") || trimmedLine.equals("") || (trimmedLine.startsWith("topic-"))) {
                continue;
            } else if(trimmedLine.startsWith("metricsReporter") && !trimmedLine.startsWith("metricsReporterList")) {
                writer.write("  - " + trimmedLine.substring(18).replace("\"", "") + System.getProperty("line.separator"));
            } else if(trimmedLine.startsWith("activeProtocol") && !trimmedLine.startsWith("activeProtocolList")) {
                writer.write("  - " + trimmedLine.substring(17).replace("\"", "")  + System.getProperty("line.separator"));
            } else if(trimmedLine.startsWith("id")) {
                writer.write("    - " + currentLine + System.getProperty("line.separator"));
            } else if(trimmedLine.startsWith("resourceId") || trimmedLine.startsWith("topic:") || trimmedLine.startsWith("type")){
                writer.write("      " + currentLine + System.getProperty("line.separator"));
            } else {
                writer.write(currentLine + System.getProperty("line.separator"));
            }

        }
        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(inputFile);

    }

    public void generateFileForEngine(WldtEngineConfigurationDescriptor wldtEngineConfigurationDescriptor) throws IOException {
        writeOnFile("conf-test/wldt.yaml", wldtEngineConfigurationDescriptor);
    }

    public void generateFileForWorkers(List<WldtWorkerConfigurationDescriptor> workers) throws IOException {

        Iterator<WldtWorkerConfigurationDescriptor> it = workers.iterator();
        WldtWorkerConfigurationDescriptor wldtWorkerConfigurationDescriptor;
        while (it.hasNext()) {
            wldtWorkerConfigurationDescriptor = it.next();

            if(wldtWorkerConfigurationDescriptor.getWorkerType().equals("MQTT2MQTT")) {
                writeOnFile("conf-test/mqtt.yaml", wldtWorkerConfigurationDescriptor);

            } else if(wldtWorkerConfigurationDescriptor.getWorkerType().equals("COAP2COAP")){
                writeOnFile("conf-test/coap.yaml", wldtWorkerConfigurationDescriptor);
            }

        }


    }

}
