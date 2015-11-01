package de.soco.dataextractor;

/**
 * Created by bthofrichter on 01.11.15.
 */
public class Main {

    public static void main(String[] args) {

        PredictionSetExtractor extractor = new PredictionSetExtractor(",",5);
        extractor.extract();

        TestSetPredictionSetExtractor testSetExtractor = new TestSetPredictionSetExtractor(",",5);
        testSetExtractor.extract();

    }
}
