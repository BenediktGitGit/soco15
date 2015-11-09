package com.springapp.mvc.extractor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bthofrichter on 08.11.15.
 */

public class ExtractorReport extends Extractor {

    private static final Class<Extractor> serviceClass = Extractor.class;

    private static Logger logger = Logger.getLogger(serviceClass);


    public ExtractorReport(String filename, int lineLength, String csvSplitBy) {
        super(filename, lineLength, csvSplitBy);
    }

    public static void getReport(String stockCloseDate, double delta, double[] denorminput, String denormOutput, String desiredOutputdenorm, Double MSE, String numTooLow, String numRight, String numTooHigh) {
        try {
            printLine();
            getOutputReport(stockCloseDate);
            printLine();
            details(delta, denorminput, denormOutput, desiredOutputdenorm, MSE, numTooLow, numRight, numTooHigh);
        }
        catch (Exception e) {
            logger.error("doReportException in " + serviceClass + e);
        }
    }

    private static void getOutputReport(String stockCloseDate) throws Exception{
        printLine();
        System.out.print("Betrachter Börsenschluss am: " + stockCloseDate);
    }

    private static void printLine() {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void details( double delta, double[] denorminput, String denormOutput, String desiredOutputdenorm, Double MSE, String numTooLow, String numRight, String numTooHigh) {
        System.out.print(" Inputneuronen: " + Arrays.toString(denorminput)+ " Outputneuron: " + denormOutput);
        System.out.println(" Tatsächlicher Wert: " + desiredOutputdenorm);
        System.out.print("Abweichung vom tatsächlichen Börsenkurs: " + delta + " Mean Squared Error: " + MSE);
        System.out.println(" Anzahl zu niedriger Vorhersagen: " + numTooLow + " Anzahl richtiger Vorhersagen: " + numRight + " Anzahl zu hoher Vorhersagen: " + numTooHigh);
    }


}
