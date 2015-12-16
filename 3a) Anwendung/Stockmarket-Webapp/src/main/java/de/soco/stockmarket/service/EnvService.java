package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 11.12.15.
 */
@Component
public class EnvService {

    @Autowired
    private Environment env;

    private static final Class<EnvService> serviceClass = EnvService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    public List<String> listFiles(String directoryName) {
        List<String> result = new ArrayList<>();
        try {
            File directory = new File(directoryName);

            File[] fList = directory.listFiles();
            for (File file : fList) {
                if (file.isFile()) {
                    result.add(file.getName());
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("listFiles Exception " + e);
        }
        return Collections.emptyList();
    }

    public String addProperty(String propertyName) {
        try {
            JSONObject object = new JSONObject();
            String propStr = env.getProperty(propertyName);
            List<String> list = Arrays.asList(propStr.split(","));
            object.put("name", list.get(0));
            object.put("value", list.get(1));
            return object.toString();
        } catch (Exception e) {
            logger.error("addProperty Exception " + e);
        }
        return "";
    }

    public String getProperty (String propertyName) {
        return env.getProperty(propertyName);
    }

}
