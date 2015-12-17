package de.soco.dataextractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Benedikt Hofrichter on 31.10.15.
 *
 *
 */
public class TestSetPredictionSetExtractor extends PredictionSetExtractor {

    public TestSetPredictionSetExtractor(String csvSplitBy, int lineLength) {
        super(csvSplitBy, lineLength);
    }


    @Override
    public void extract(String setType) {
        getStockDataRawAsc();
        createFileName(setType);
        System.out.print("creating file: " + this.targetLocation + " . . .");
        generateCsvFileAsc(this.targetLocation, this.csvSplitBy);
        System.out.print("done.\n");
    }

    @Override
    protected void createFileName(String setType) {
        this.filename = "StockMarket" + "-" + this.getBeginDate() + "_" + this.getEndDate() + "-" + setType + ".csv";
        this.targetLocation = this.prefix + this.filename;
    }


    @Override
    protected void generateCsvFileAsc(String fileName, String delimiter) {
        try {
            String[] lineValueSet = new String[this.lineLength];
            RandomAccessFile fileWriter = new RandomAccessFile(new File(fileName), "rw");

            for (int i = lineLength; i < this.RawDataAsc.size(); i++) {
                String[] singleLine = this.RawDataAsc.get(i);
                for (int j = i - lineLength + 1; j <= i; j++) {
                    fileWriter.write(this.RawDataAsc.get(j)[1].getBytes());
                    if (j != i) {
                        fileWriter.write(csvSplitBy.getBytes());
                    }
                }
                fileWriter.write("\n".getBytes());
            }
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

//    @Override
//    protected void generateCsvFileDesc(String fileName, String delimiter) {
//        try {
//            String[] lineValueSet = new String[this.lineLength];
//            FileWriter fileWriter = new FileWriter(fileName);
//            int counter=0;
//            int rowCounter=0;
//
//            do {
//                this.br = new BufferedReader(new FileReader(this.csvLine));
//                for(int j = 0; j<rowCounter; j++) {
//                    line = br.readLine();
//                }
//                // fill lineSet
//                for (int i = 0; i < this.lineLength; i++) {
//                    line = br.readLine();
//                    if (line != null) {
//                        lineValueSet[i] = line;
//                        counter++;
//                    }
//                }
//
//                // Nur vollständige Zeilen in csv übernehmen
//                if (counter == this.lineLength) {
//                    String[] singleLine = lineValueSet[0].split(csvSplitBy);
//                    for (int i = lineLength - 1; i >= 0; i--) {
//                        singleLine = lineValueSet[i].split(this.csvSplitBy);
//                        fileWriter.append(singleLine[1]);
//                        if (i!=0) {
//                            fileWriter.append(",");
//                        }
//                    }
//                    fileWriter.append('\n');
//                    counter = 0;
//                }
//                rowCounter++;
//                fileWriter.flush();
//                br.close();
//            } while (line != null);
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
