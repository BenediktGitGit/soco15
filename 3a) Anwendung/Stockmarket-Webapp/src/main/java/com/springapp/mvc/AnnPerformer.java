package com.springapp.mvc;

import com.springapp.mvc.extractor.Extractor;
import com.springapp.mvc.extractor.ExtractorReport;
import com.springapp.mvc.extractor.PredictionSetExtractor;
import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bthofrichter on 08.11.15.
 */
public class AnnPerformer {

    private static final Class<AnnPerformer> serviceClass = AnnPerformer.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    private NeuralNetwork ann;

    public double MSE;  //Zur Berrechnung des MSE des trainierten KNN.

    public Integer numoverall; //Die Anzahl der bisher verarbeiteten Börsentage.

    public Integer numtoolow;  //Die Anzahl der zu niedrigen Vorhersagen (Abweichung < -20).

    public Integer numright;   //Die Anzahl der richtigen Vorhersagen (Abweichung maximal +/- 20).

    public Integer numtoohigh;  //Die Anzahl der zu hohen Vorhersagen (Abweichung > 20).

    protected Double denormOutput;

    protected Double denormdesiredoutput;

    protected double[] denorminput;

    protected double delta;
    
    protected DataSet predictionSet;

    private String[] line;

    private List<String> dateList;

    private PredictionSetExtractor predictionSetExtractor;


    public AnnPerformer(String annLocation, int dataSetInput, int dataSetOutput) {
        this.MSE = 0d;
        this.numoverall = 0;
        this.numtoolow = 0;
        this.numright = 0;
        this.numtoohigh = 0;
        this.denormOutput = 0d;
        this.denormdesiredoutput = 0d;
        setMuliPerceptron(annLocation);
        this.predictionSet = new DataSet(dataSetInput, dataSetOutput);
        this.predictionSetExtractor = getPredictionSetExtractor("PredictionSet",dataSetInput+dataSetOutput,",");
        processExtractor();
    }

    private void processExtractor() {
        predictionSetExtractor.extract();
        dateList = predictionSetExtractor.getDateList();
        writePredictionToSet();
        annOperating();
    }

    private void writePredictionToSet() {
        for (int i = 0; i < predictionSetExtractor.getRawDataAsc().size() ; i++)
        {
            //Setze den Input sowie den tatsächlichen Output (der tatsächliche Börsenkurs).
            predictionSet.addRow(new DataSetRow(new double[]{Double.parseDouble(line[1]),
                    Double.parseDouble(line[2]), Double.parseDouble(line[3]), Double.parseDouble(line[4])}, new double[]{Double.parseDouble(line[5])}));
        }
    }

    private void annOperating() {
        //Über die einzelnen Zeilen des Datensatze iterieren.
        for (DataSetRow row : predictionSet.getRows())
        {
            //Neuer Zeile wird gelesen, also Anzahl verarbeiter Zeilen des Datensatzes + 1.
            numoverall++;
            //Lese eine Zeile in das KNN und berechne den Output.
            ann.setInput(row.getInput());
            ann.calculate();

            //Die Werte müssen nun denormalisiert werden.
            //Denormalisiere den Output des Ausgabeneurons
            denormOutput = Array.getDouble(ann.getOutput(), 0)*predictionSetExtractor.getDaxMax();
            //Denormalisiere den gewünschten Output,
            denormdesiredoutput = Array.getDouble(row.getDesiredOutput(),0)*predictionSetExtractor.getDaxMax();

            //Denormalisiere den Input
            denorminput = row.getInput();
            for(int i = 0; i < denorminput.length;i++) {
                denorminput[i] = denorminput[i] * predictionSetExtractor.getDaxMax();
            }

            //Kalkuliere Delta und MSE
            setDelta(denormOutput, denormdesiredoutput);

            setMSE(numoverall);

            //Kalkuliere Vorhersagegüte
            calcPredictionValidation(20);

            ExtractorReport.getReport(predictionSetExtractor.getDateList().get(numoverall), delta, denorminput, denormOutput.toString(), denormdesiredoutput.toString(), MSE, numtoolow.toString(), numright.toString(), numtoohigh.toString());
        }
    }

    private void calcPredictionValidation(int val) {
        try {
            if (delta < val) {
                numtoolow = numtoolow + 1;
            } else if (delta > val) {
                numtoohigh = numtoohigh + 1;
            } else {
                numright = numright + 1;
            }
        }
        catch (NullPointerException ne) {
            logger.error("calcPredictionValidationException in " + serviceClass + ne);
        }
    }

    private void setDelta(Double output, Double desOutput) {
        this.delta = output - desOutput;
    }

    public void setMSE(int numoverall) {
        this.MSE = Math.pow(delta,2)/numoverall;
    }

    private PredictionSetExtractor getPredictionSetExtractor(String filename, int lineLength, String delimiter) {
        if(this.predictionSetExtractor == null) {
            this.predictionSetExtractor = new PredictionSetExtractor(filename, lineLength, delimiter);
        }
        return this.predictionSetExtractor;
    }

    private void setMuliPerceptron(String filepath) {
            this.ann = new MultiLayerPerceptron().load(filepath);
    }

}
