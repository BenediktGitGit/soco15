package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */
@Component
public class FormatService {

    @Autowired
    private Environment env;

    private static final Class<FormatService> serviceClass = FormatService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    public FormatService() {
    }

    public List<List<String>> formatToCloseData(List<String> data, Integer periodLength) {
        try {
            List<List<String>> result = new ArrayList<>();

            Integer closePos = 4;
            for(int i = periodLength-1; i< data.size(); i++) {
                List<String> line = new ArrayList<>();
                String[] splitted = data.get(i).split(",");
                String datum = splitted[0];
                line.add(datum);
                for (int z = i-periodLength+1; z <= i; z++) {
                    line.add(data.get(z).split(",")[closePos]);
                }
                result.add(line);
            }
            return result;
        }
        catch (Exception e) {
            logger.error("formatToCloseData in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    public List<List<String>> addCols(List<List<String>> data, List<List<String>> elem) {
        try {
            List<List<String>> result = data;
            for (int i = 0; i < result.size(); i++) {
                List<String> el = elem.get(i);
                for (String s : el) {
                    result.get(i).add(s);
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("addCols in " + serviceClass + e);
        }
        return Collections.emptyList();
    }


    public JSONArray parseResponseList(List<List<String>> data) {
        try {
            JSONArray result = new JSONArray();
            Integer pl = Integer.parseInt(env.getProperty("format.period.length"));
            for(List<String> ls : data) {
                JSONObject o = new JSONObject();
                o.put("date", ls.get(0));
                JSONArray a = new JSONArray();
                for(int i = 1; i<pl; i++) {
                    a.put(ls.get(i));
                }
                o.put("input", a);
                o.put("ro", ls.get(pl));
                o.put("po", ls.get(pl+1));

                JSONObject ob = new JSONObject();
                ob.put("under", ls.get(pl+2));
                ob.put("equal", ls.get(pl+3));
                ob.put("over", ls.get(pl+4));
                o.put("dist", ob);
                o.put("mse", ls.get(pl+5));
                result.put(o);
            }
            return result;
        } catch (Exception e)
        {
            logger.error("parseResponseList Exception " + e);
        }
        return new JSONArray();
    }

}
