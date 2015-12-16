package de.soco.dataextractor;

/**
 * Created by Benedikt Hofrichter on 31.10.15.
 *
 */
public class Main {

    public static void main(String[] args) {

        PredictionSetExtractor extractor = new PredictionSetExtractor(",",5);
        extractor.extract("PredictionSet");

        TestSetPredictionSetExtractor testSetExtractor = new TestSetPredictionSetExtractor(",",5);
        testSetExtractor.extract("TestSet");

        double maximum = extractor.daxMax;
        System.out.println(maximum);
    }
}
