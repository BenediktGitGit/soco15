package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */
@Component
public class FormatService {

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

    public List<String> addDistVals(List<String> qdata) {
           // return Date _ _ _ _ ro, po, under, equal, over
        try {

        }
        catch (Exception e) {
            logger.error("addDistVals in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    public List<String> addMSE(List<String> qdata) {
        // return Date _ _ _ _ ro, po, under, equal, over, MSE
        try {

        }
        catch (Exception e) {
            logger.error("addMSE in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

}
