package de.soco.stockmarket.service;

import org.apache.log4j.Logger;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */

@Component
public class AnnService {

    @Autowired
    private Environment env;

    @Autowired
    private FormatService formatService;

    private static final Class<AnnService> serviceClass = AnnService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    private  NeuralNetwork ann_mlp;

//    private BigDecimal cur_mse = new BigDecimal(0.0);

    public AnnService() {
    }

    public List<List<String>> testWithAnn(List<List<String>> data, String ann_name) {
        List<List<String>> poList = new ArrayList<>();
        double ro = 0d;

        try {
            String path = env.getProperty("media.source.base") + env.getProperty("media.source.ann") + "/" + ann_name;
            Integer pl = Integer.parseInt(env.getProperty("format.period.length"));
            Double conf = Double.parseDouble(env.getProperty("dist.conf"));

            DataSet testSet = new DataSet(pl - 1, 1);

            for (List<String> ls : data) {
                double d[] = new double[pl-1];
                for (int i = 0; i < pl - 1; i++) {
                    d[i] = Double.parseDouble(ls.get(i + 1));
                }
                ro = Double.parseDouble(ls.get(pl));
                testSet.addRow(new DataSetRow(d, new double[]{ro}));
            }

            ann_mlp = MultiLayerPerceptron.createFromFile(path);


            List<DataSetRow> dataRows = testSet.getRows();

            for (int i = 0; i< dataRows.size(); i++) {
                List<String> tmp = new ArrayList<>();
                double [] input = dataRows.get(i).getInput();
                ann_mlp.setInput(input);
                ann_mlp.calculate();
                double po = Array.getDouble(ann_mlp.getOutput(), 0);
//                BigDecimal Po = new BigDecimal(Double.toString(po)).setScale(16, BigDecimal.ROUND_HALF_UP);
//                ro = Double.parseDouble(data.get(i).get(pl));
                tmp.add(String.valueOf(po));
                poList.add(tmp);
                System.out.print("Input: " + Arrays.toString(dataRows.get(i).getInput()));
                System.out.println(" Output: " + String.valueOf(po));

            }
            return formatService.addCols(data, poList);
        }
        catch (Exception e) {
            logger.error("testWithAnn Exception in " + serviceClass + e);
        }
        return Collections.emptyList();
    }

}
