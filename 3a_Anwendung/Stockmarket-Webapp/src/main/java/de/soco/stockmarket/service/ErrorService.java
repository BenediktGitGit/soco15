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
            List<List<String>> allList = new ArrayList<>();

            for(int i=0; i<data.size(); i++) {
                List<String> tmp = new ArrayList<>();
                tmp.addAll(getDist(i, data));
                tmp.addAll(addErrorMse(i, data));
                tmp.addAll(addAccuracy(i, data));
                allList.add(tmp);
            }
            cur_mse = new BigDecimal(0.0);

            return formatService.addCols(data, allList);
        }
        catch (Exception e) {
                logger.error("getAllErrorMessures Exception in " + serviceClass + e);
        }
         return Collections.emptyList();
    }


    public List<String> getDist(int i, List<List<String>> data) {
        Integer pl = Integer.parseInt(env.getProperty("format.period.length"));
        double confidence = Double.parseDouble(env.getProperty("dist.conf"));

        List<String> list;
        try {
            BigDecimal Ro = new BigDecimal(data.get(i).get(pl));
            BigDecimal Po = new BigDecimal(data.get(i).get(pl+1));

            list = getDistList(Ro.doubleValue(), Po.doubleValue(), confidence);
            return list;
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

    public List<String> addErrorMse(int i, List<List<String>> data) {
        try {
            Integer precision = Integer.parseInt(env.getProperty("format.data.denorm.precision"));
            Integer xPos = Integer.parseInt(env.getProperty("format.period.length")); // ro_pos
            Integer yPos = xPos + 1; // po_pos


            List<String> list = new ArrayList<>();
            BigDecimal cur = new BigDecimal(0);

                BigDecimal x = new BigDecimal(data.get(i).get(xPos)).setScale(precision, BigDecimal.ROUND_HALF_UP);
                BigDecimal y = new BigDecimal(data.get(i).get(yPos)).setScale(precision, BigDecimal.ROUND_HALF_UP);
                BigDecimal Eins = new BigDecimal(1);
                BigDecimal Div = new BigDecimal(i+1);
                BigDecimal Diff =  (x.subtract(y)).abs().setScale(precision, BigDecimal.ROUND_HALF_UP);
                BigDecimal Pow = Diff.pow(2);
                BigDecimal Dividend = Eins.divide(Div, precision, RoundingMode.HALF_UP);
                BigDecimal Mse = Dividend.multiply(Pow);
                cur_mse = cur_mse.add(Mse).setScale(precision, BigDecimal.ROUND_HALF_UP);

                list.add(cur_mse.toString());

            return list;
        }
        catch (Exception e) {
            logger.error("addMSE Exception in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    public List<String> addAccuracy(int i, List<List<String>> data) {
        Integer pl = Integer.parseInt(env.getProperty("format.period.length"));
        Integer acc_prec = Integer.parseInt(env.getProperty("format.data.error.acc.precision"));

        List<String> tmp = new ArrayList<>();

        try {
                BigDecimal Ro = new BigDecimal(data.get(i).get(pl)).setScale(acc_prec, BigDecimal.ROUND_HALF_UP);
                BigDecimal Po = new BigDecimal(data.get(i).get(pl+1)).setScale(acc_prec, BigDecimal.ROUND_HALF_UP);
                BigDecimal Diff = Ro.divide(Po, acc_prec, RoundingMode.HALF_UP);

                if(Diff.compareTo(BigDecimal.valueOf(1)) == 1) {
                    Diff = Po.divide(Ro, acc_prec, RoundingMode.HALF_UP);
                    Diff = Diff.multiply(BigDecimal.valueOf(100));
                }
                else {
                    Diff = Diff.multiply(BigDecimal.valueOf(100));
                }
                tmp.add(Diff.toString());

            return tmp;
        }
        catch (Exception e) {
            logger.error("addAccuracy Exception in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

}
