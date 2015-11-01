package de.soco.dataextractor;

import java.io.*;

/**
 * Created by Benedikt Hofrichter on 31.10.15.
 *
 *
 */


public class PredictionSetExtractor {

    protected String csvLine  = "resources/YAHOO-INDEX_GDAXI_RAW.csv";

    protected String prefix = "resources/analyzed/StockMarket";

    protected int columnLength;

    public String beginDate = "";

    public String endDate = "";

    protected BufferedReader br;

    protected String line = "";

    protected String csvSplitBy;

    protected int lineLength;

    public PredictionSetExtractor(String csvSplitBy, int lineLength) {
        this.csvSplitBy = csvSplitBy;
        this.lineLength = lineLength;
        this.columnLength = 0;
    }

    public void extract() {
        this.setColumnLength();
        String name = this.createFileName();
        System.out.print("creating file: " + name + " . . .");
        this.generateCsvFile(name, csvSplitBy);
        System.out.print("done.\n");
    }


    protected String createFileName() {
        setBeginDate();
        setEndDate();
        String fileName = this.prefix + "-" + getBeginDate() + "_" + getEndDate() + "-" + "Prediction_Set" + ".csv";
        return fileName;
    }



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
                        fileWriter.append(singleLine[0]);
                        fileWriter.append(",");
                    for (int i = lineLength - 1; i >= 0; i--) {
                        singleLine = lineValueSet[i].split(csvSplitBy);
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

    protected void setEndDate() {
        try {
            br = new BufferedReader(new FileReader(csvLine));
            line = br.readLine();
            String[] valueRow = line.split(csvSplitBy);
            this.endDate = valueRow[0];
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("setEndDateException: " + e);
        } catch (IOException e) {
            System.out.println("setEndDateException: " + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("setEndDateException: " + e);
                }
            }
        }
    }

    protected void setColumnLength() {
        try {
            br = new BufferedReader(new FileReader(csvLine));
            while ((line = br.readLine()) != null) {
                this.columnLength++;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("setColumnLengthException: " + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("setColumnLengthException: " + e);
                }
            }
        }
    }


    protected void setBeginDate() {
        try {
            br = new BufferedReader(new FileReader(csvLine));
            for(int i = 0; i < this.columnLength-this.lineLength+1; i++) {
                line = br.readLine();
            }
            String[] valueRow = line.split(csvSplitBy);
            this.beginDate = valueRow[0];
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("setBeginDateException: " + e);
        } catch (IOException e) {
            System.out.println("setBeginDateException: " + e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("setBeginDateException: " + e);
                }
            }
        }
    }

    public String getEndDate() {
        return endDate;
    }

    public String getBeginDate() {
        return beginDate;
    }


}
