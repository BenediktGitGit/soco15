package de.soco.stockmarket.service;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */
@Component
public class SaveService {

    @Autowired
    private Environment env;

    private static final Class<SaveService> serviceClass = SaveService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    private static final String COMMA_DELIMITER = ",";

    private static final String NEW_LINE_SEPARATOR = "\n";

    public SaveService() {
    }

    public void saveData(List<List<String>> data, String dataset, String postFix) {
        FileWriter fileWriter = null;
        try {
                fileWriter = new FileWriter(getFilename(data, dataset, postFix));

                for (List<String> ls : data) {
                    for (String s : ls) {
                        fileWriter.append(s);
                        fileWriter.append(COMMA_DELIMITER);
                    }
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
           }
        catch (Exception e) {
            logger.error("saveData in " + serviceClass + e);
        }
        finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                logger.error("saveData while flush.");
                e.printStackTrace();
            }
        }
    }

    private String getFilename (List<List<String>> data, String dataset, String postFix) {
        try {
            String base_url = env.getProperty("media.source.analyzed");
            Integer periodLengh = Integer.parseInt(env.getProperty("format.period.length"));

            String result = base_url + data.get(0).get(0) + "_" + data.get(data.size()-1).get(0) + "_" + dataset.replace("/", "_");
            if(postFix != "") {
                result += "_" + postFix;
            }
            result += ".csv";
            return result;
        }
        catch (Exception e) {
            logger.error("getFilename in " + serviceClass + e);
        }
        return "";
    }

}
