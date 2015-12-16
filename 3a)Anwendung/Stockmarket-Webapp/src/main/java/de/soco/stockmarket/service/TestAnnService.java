package de.soco.stockmarket.service;

import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 09.12.15.
 */

@Component
public class TestAnnService {

    @Autowired
    private Environment env;

    private static final Class<TestAnnService> serviceClass = TestAnnService.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    public TestAnnService() {
    }

    public List<List<String>> testWithAnn(List<List<String>> data, String ann_name) {
        // return Date _ _ _ _ ro, po
        try {
            String path = env.getProperty("media.source.base") + "/" + ann_name;
            Integer pl = Integer.parseInt(env.getProperty("format.period.length"));

            DataSet testSet = new DataSet(pl - 1, 1);
            for (List<String> ls : data) {
                double d[] = new double[pl];
                for (int i = 0; i < pl - 2; i++) {
                    d[i] = Double.parseDouble(ls.get(i + 1));
                }
                double ro = Double.parseDouble(ls.get(pl - 1));
                testSet.addRow(new DataSetRow(d, new double[]{ro}));
            }

            NeuralNetwork ann_mlp = NeuralNetwork.createFromFile(path);


            for (DataSetRow dataRow : testSet.getRows()) {
                ann_mlp.setInput(dataRow.getInput());
                ann_mlp.calculate();
                double[] po = ann_mlp.getOutput();
                System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
                System.out.println(" Output: " + Arrays.toString(po) );
            }
            // add po to List

        }
        catch (Exception e) {
            logger.error("testWithAnn in " + serviceClass + e);
        }
        return Collections.emptyList();
    }
}
