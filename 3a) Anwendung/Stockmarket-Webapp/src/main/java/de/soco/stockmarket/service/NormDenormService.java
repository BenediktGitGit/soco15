package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */
@Component
public class NormDenormService {

    @Autowired
    private Environment env;

    private static final Class<NormDenormService> serviceClass = NormDenormService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    public NormDenormService() {
    }

    public List<List<String>> normalize(List<List<String>> data, Boolean date) {
        try {
            List<List<String>> result = new ArrayList<>();

            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");

            String formula = env.getProperty("normalize.formula");

            Double min = 200000d;
            Double max = 0d;

            for (List<String> ls : data) {
                for (int i = 1; i<ls.size(); i++) {
                    Double cur_val = Double.parseDouble(ls.get(i));
                   if (cur_val > max) {
                       max = cur_val;
                   }
                   if (cur_val < min) {
                       min = cur_val;
                   }
                }
            }

            engine.put("MIN", min);
            engine.put("MAX", max);

            for (List<String> ls : data) {
                List<String> line = new ArrayList<>();
                if(date) {
                    line.add(ls.get(0));
                }
                for (int i = 1; i<ls.size(); i++) {
                    engine.put("X", Double.parseDouble(ls.get(i)));
                    line.add(engine.eval(formula).toString());
                }
                result.add(line);
            }
            return result;
        }
        catch (Exception e) {
            logger.error("normalize in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    public List<String> denormalize(List<String> data_norm) {
        try {

        }
        catch (Exception e) {
            logger.error("denormalize in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

}
