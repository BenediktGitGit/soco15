package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.Normalizer;
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

    public AnnService() {
    }

    public List<List<String>> testWithAnn(List<List<String>> data, String ann_name) {
        // return Date _ _ _ _ ro, po
        List<List<String>> poList = new ArrayList<>();
        double ro = 0d;

        try {
            String path = env.getProperty("media.source.base") + env.getProperty("media.source.ann") + "/" + ann_name;
            Integer pl = Integer.parseInt(env.getProperty("format.period.length"));

            DataSet testSet = new DataSet(pl - 1, 1);

            for (List<String> ls : data) {
                double d[] = new double[pl-1];
                for (int i = 0; i < pl - 1; i++) {
                    d[i] = Double.parseDouble(ls.get(i + 1));
                }
                ro = Double.parseDouble(ls.get(pl));
                testSet.addRow(new DataSetRow(d, new double[]{ro}));
            }

            File netPath = new File(path);

            NeuralNetwork ann_mlp = NeuralNetwork.createFromFile(netPath);

            List<DataSetRow> dataRows = testSet.getRows();
            for (int i = 0; i< dataRows.size(); i++) {
                ann_mlp.setInput(dataRows.get(i).getInput());
                ann_mlp.calculate();
                double[] po = ann_mlp.getOutput();

                poList.get(i).add(String.valueOf(po));
                System.out.print("Input: " + Arrays.toString(dataRows.get(i).getInput()) );
                System.out.println(" Output: " + Arrays.toString(po) );

            }
            return formatService.addCols(data, poList);
        }
        catch (Exception e) {
            logger.error("testWithAnn Exception in " + serviceClass + e);
        }
        return Collections.emptyList();
    }
}
