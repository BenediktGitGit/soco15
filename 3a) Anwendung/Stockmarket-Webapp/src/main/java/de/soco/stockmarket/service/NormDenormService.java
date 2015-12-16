package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by bthofrichter on 09.12.15.
 */
@Component
public class NormDenormService {

    @Autowired
    private Environment env;

    @Autowired
    private FormatService formatService;

    private static final Class<NormDenormService> serviceClass = NormDenormService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    private Integer pl;

    public BigDecimal min = new BigDecimal(200000.0);

    public BigDecimal max = new BigDecimal(0.0);

    public NormDenormService() {

    }

    public List<List<String>> processFormula(List<List<String>> data, Boolean date, String formula, BigDecimal MIN, BigDecimal MAX, List<Integer> excludeIds, Integer precision) {
        try {
            List<List<String>> result = new ArrayList<>();

            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");


                engine.put("MIN", MIN);
                engine.put("MAX", MAX);

                for(List<String> ls : data)
                {
                    List<String> line = new ArrayList<>();
                    if (date) {
                        line.add(ls.get(0));
                    }
                    for (int i = 1; i < ls.size(); i++) {
                        if (!excludeIds.contains(i)) {
                            engine.put("X", Double.parseDouble(ls.get(i)));
                            BigDecimal val = new BigDecimal(engine.eval(formula).toString()).setScale(precision, BigDecimal.ROUND_HALF_UP);
                            line.add(String.valueOf(val));
                        }
                        else {
                            line.add(ls.get(i));
                        }
                    }
                    result.add(line);
                }

                return result;
            }
            catch (Exception e) {
            logger.error("processFormula in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    public List<List<String>> normalize(List<List<String>> data, Boolean date) {
        try {
            String formula = env.getProperty("normalize.formula");
            Integer precision = Integer.parseInt(env.getProperty("format.data.norm.precision"));
            pl = Integer.parseInt(env.getProperty("format.period.length"));

            for (List<String> ls : data) {
                for (int i = 1; i<pl+1; i++) {
                    BigDecimal cur_val = new BigDecimal(ls.get(i));

                    if (cur_val.compareTo(max) > 0) {
                        max = cur_val;
                    }
                    else if (cur_val.compareTo(min) < 0) {
                        min = cur_val;
                    }
                }
            }
            return processFormula(data, date, formula, min, max, new ArrayList<Integer>(), precision);
        }
        catch (Exception e) {
            logger.error("normalize in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

    public List<List<String>> denormalize(List<List<String>> data,  Boolean date) {
        try {
            String formula = env.getProperty("denormalize.formula");
            Integer precision = Integer.parseInt(env.getProperty("format.data.denorm.precision"));
            pl = Integer.parseInt(env.getProperty("format.period.length"));
            List ids = new ArrayList();
            ids.add(pl+2);
            ids.add(pl+3);
            ids.add(pl+4);
            return processFormula(data, date, formula, min, max, ids, precision);
        }
        catch (Exception e) {
            logger.error("denormalize in " + serviceClass + e);
        }
        return Collections.emptyList();
    }


    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }
}
