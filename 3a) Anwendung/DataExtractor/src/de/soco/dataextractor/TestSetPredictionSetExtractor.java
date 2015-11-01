package de.soco.dataextractor;

import java.io.*;

/**
 * Created by bthofrichter on 01.11.15.
 */
public class TestSetPredictionSetExtractor extends PredictionSetExtractor {

    public TestSetPredictionSetExtractor(String csvSplitBy, int lineLength) {
        super(csvSplitBy, lineLength);
    }

    @Override
    public void extract() {
        super.setColumnLength();
        String name = this.createFileName();
        System.out.print("creating file: " + name + " . . .");
        this.generateCsvFile(this.createFileName(), this.csvSplitBy);
        System.out.print("done.\n");
    }

    @Override
    protected String createFileName() {
        setBeginDate();
        setEndDate();
        String fileName = this.prefix + "-" + this.getBeginDate() + "_" + this.getEndDate() + "-" + "Test_Set" + ".csv";
        return fileName;
    }

    @Override
    protected void generateCsvFile(String fileName, String delimiter) {
        try {
            String[] lineValueSet = new String[this.lineLength];
            FileWriter fileWriter = new FileWriter(fileName);
            int counter=0;
            int rowCounter=0;

            do {
                this.br = new BufferedReader(new FileReader(this.csvLine));
                for(int j = 0; j<rowCounter; j++) {
                    line = br.readLine();
                }
                // fill lineSet
                for (int i = 0; i < this.lineLength; i++) {
                    line = br.readLine();
                    if (line != null) {
                        lineValueSet[i] = line;
                        counter++;
                    }
                }

                // Nur vollständige Zeilen in csv übernehmen
                if (counter == this.lineLength) {
                    String[] singleLine = lineValueSet[0].split(csvSplitBy);
                    for (int i = lineLength - 1; i >= 0; i--) {
                        singleLine = lineValueSet[i].split(this.csvSplitBy);
                        fileWriter.append(singleLine[1]);
                        if (i!=0) {
                            fileWriter.append(",");
                        }
                    }
                    fileWriter.append('\n');
                    counter = 0;
                }
                rowCounter++;
                fileWriter.flush();
                br.close();
            } while (line != null);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
