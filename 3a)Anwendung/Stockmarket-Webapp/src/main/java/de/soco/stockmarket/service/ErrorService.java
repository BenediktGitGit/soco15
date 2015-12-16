package de.soco.stockmarket.service;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 16.12.15.
 */
@Component
public class ErrorService {

    @Autowired
    private Environment env;

    @Autowired
    private FormatService formatService;

    private static final Class<ErrorService> serviceClass = ErrorService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    private BigDecimal cur_mse = new BigDecimal(0.0);

    public List<List<String>> getAllErrorMessures(List<List<String>> data) {
        try {
            List<List<String>> dist = getDist(data);
            List<List<String>> mse = addErrorMse(dist);
            return mse;
        }
        catch (Exception e) {
                logger.error("getAllErrorMessures Exception in " + serviceClass + e);
        }
         return Collections.emptyList();
    }


    public List<List<String>> getDist(List<List<String>> data) {
        Integer pl = Integer.parseInt(env.getProperty("format.period.length"));
        double confidence = Double.parseDouble(env.getProperty("dist.conf"));

        List<List<String>> distList = new ArrayList<>();

        try {
            for(int i=0; i<data.size(); i++) {
            BigDecimal Ro = new BigDecimal(data.get(i).get(pl));
            BigDecimal Po = new BigDecimal(data.get(i).get(pl+1));

            distList.add(getDistList(Ro.doubleValue(), Po.doubleValue(), confidence));
        }

            return formatService.addCols(data, distList);
        }
        catch (Exception e) {
            logger.error("getDist Exception in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    private List<String> getDistList(double ro, double po, double confidence) {
        List<String> dist = new ArrayList<>();
        final double CONF = Math.abs(confidence);
        final String ZERO = String.valueOf(0);
        final String ONE = String.valueOf(1);
        if ( ro > po && ro - CONF > po ) { // under 1, 0 , 0
            dist.add(ONE);
            dist.add(ZERO);
            dist.add(ZERO);
        }
        else if (ro < po && ro + CONF < po) { // over 0, 0, 1
            dist.add(ZERO);
            dist.add(ZERO);
            dist.add(ONE);
        }
        else { // equal 0, 1, 0
            dist.add(ZERO);
            dist.add(ONE);
            dist.add(ZERO);
        }
        return dist;
    }

    public List<List<String>> addErrorMse(List<List<String>> data) {
        try {
            Integer precision = Integer.parseInt(env.getProperty("format.data.denorm.precision"));
            Integer xPos = Integer.parseInt(env.getProperty("format.period.length")); // ro_pos
            Integer yPos = xPos + 1; // po_pos

            List<List<String>> mseList = new ArrayList<>();
            BigDecimal cur = new BigDecimal(0);
            for(int i=0; i<data.size(); i++) {
                BigDecimal x = new BigDecimal(data.get(i).get(xPos)).setScale(precision, BigDecimal.ROUND_HALF_UP);
                BigDecimal y = new BigDecimal(data.get(i).get(yPos)).setScale(precision, BigDecimal.ROUND_HALF_UP);
                BigDecimal Eins = new BigDecimal(1);
                BigDecimal Div = new BigDecimal(i+1);
                BigDecimal Diff =  (x.subtract(y)).abs().setScale(precision, BigDecimal.ROUND_HALF_UP);
                BigDecimal Pow = Diff.pow(2);
                BigDecimal Dividend = Eins.divide(Div, precision, RoundingMode.HALF_UP);
                BigDecimal Mse = Dividend.multiply(Pow);
                cur = cur.add(Mse).setScale(precision, BigDecimal.ROUND_HALF_UP);
                List<String> list = new ArrayList<>();
                list.add(cur.toString());
                mseList.add(list);
            }
            return formatService.addCols(data, mseList);
        }
        catch (Exception e) {
            logger.error("addMSE Exception in " + serviceClass + e);
        }
        return Collections.emptyList();
    }


}
