package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */
@Component
public class DataService {

    @Autowired
    private FormatService formatService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private  NormDenormService normDenormService;
    
    @Autowired
    private TestAnnService testAnnService;

    private static final Class<DataService> serviceClass = DataService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    private List<String> qdata = new ArrayList<>();

    private List<String> qdata_format = new ArrayList<>();

    private List<String> qdata_format_norm = new ArrayList<>();


    public DataService() {
    }

     public List<List<String>> extractCloseData (Integer periodLength) {
        try {
            return formatService.formatToCloseData(qdata, periodLength);
        } catch (Exception e)
        {
         logger.error("extractCloseData Exception " + e);
        }
       return Collections.emptyList();
    }

    public void saveData(List<List<String>> data, String stock, String postFix) {
        try {
            saveService.saveData(data, stock, postFix);
    } catch (Exception e)
    {
        logger.error("saveData Exception " + e);
    }
    }

    public List<List<String>> normalize(List<List<String>> data, Boolean withDate) {
        try {
            return normDenormService.normalize(data, withDate);
        } catch (Exception e)
        {
            logger.error("normalize Exception " + e);
        }
        return Collections.emptyList();
    }

    public List<List<String>> testWithAnn (List<List<String>> data, String ann_name) {
        try {
            return testAnnService.testWithAnn(data, ann_name);
        } catch (Exception e)
        {
            logger.error("testWithAnn Exception " + e);
        }
        return Collections.emptyList();
    }

    public List<String> addDistVals () {
        try {
            qdata_format = formatService.addDistVals(qdata_format);
        } catch (Exception e)
        {
            logger.error("addDistVals Exception " + e);
        }
        return Collections.emptyList();
    }

    public List<String> deNormalize() {
        try {
            qdata_format = normDenormService.denormalize(qdata_format_norm);
        } catch (Exception e)
        {
            logger.error("deNormalize Exception " + e);
        }
        return Collections.emptyList();
    }

    public List<String> addMSE () {
        try {
         qdata_format = formatService.addMSE(qdata_format);
        } catch (Exception e)
        {
            logger.error("addMSE Exception " + e);
        }
        return Collections.emptyList();
    }

    /*********Properties***********/
    public List<String> getQdata_norm() {
        return qdata_format_norm;
    }

    public void setQdata_norm(List<String> qdata_norm) {
        this.qdata_format_norm = qdata_norm;
    }

    public List<String> getQdata() {
        return qdata;
    }

    public void setQdata(List<String> qdata) {
        this.qdata = qdata;
    }


}
