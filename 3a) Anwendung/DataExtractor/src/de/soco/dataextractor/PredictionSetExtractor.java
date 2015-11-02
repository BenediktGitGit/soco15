package de.soco.dataextractor;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benedikt Hofrichter on 31.10.15.
 *
 *
 */


public class PredictionSetExtractor {

    protected String csvLine  = "resources/YAHOO-INDEX_GDAXI_RAW.csv";

    protected String prefix = "resources/analyzed/";

    protected List<String[]> RawDataAsc;

    public double daxMax;

    public String filename = "";

    public String targetLocation = "";

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
    }


    public void getValueInList() {
        this.daxMax = 0.0d;
        for(int i = 1; i < this.RawDataAsc.size(); i++) {
            double cur = Double.parseDouble(this.RawDataAsc.get(i)[1]);
            if (cur > this.daxMax) {
                this.daxMax = cur;
            }
        }
    }

    public void extract(String setType) {
        getStockDataRawAsc();
        createFileName(setType);
        System.out.print("creating file: " + this.targetLocation + " . . .");
        generateCsvFileAsc(this.targetLocation, csvSplitBy);
        System.out.print("done.\n");
    }


    protected void createFileName(String setType) {
        this.filename = "StockMarket" + "-" + this.getBeginDate() + "_" + this.getEndDate() + "-" + setType + ".csv";
        this.targetLocation = this.prefix +  this.filename;
    }

    protected List<String[]> getStockDataRawDesc() {
        List<String[]> tmp = new ArrayList<String[]>();
        try {
            CSVReader reader = new CSVReader(new FileReader(this.csvLine));
            String[] elements;
            do {
                elements = reader.readNext();
                tmp.add(elements);
            } while (elements != null);
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
        return tmp;
    }

    protected void getStockDataRawAsc() {
        List<String[]> tmp = new ArrayList<String[]>();
        tmp = this.getStockDataRawDesc();
        this.RawDataAsc = new ArrayList<String[]>(tmp.size());
        for (int i = tmp.size()-1; i >= 0 ; i--) {
            this.RawDataAsc.add(tmp.get(i));
        }
        setBeginDate(this.RawDataAsc.get(lineLength)[0]);
        setEndDate(this.RawDataAsc.get(this.RawDataAsc.size()-1)[0]);
        setColumnLength(this.RawDataAsc.size());
        getValueInList();
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    protected void generateCsvFileAsc(String fileName, String delimiter) {
        try {
            String[] lineValueSet = new String[this.lineLength];
            RandomAccessFile fileWriter = new RandomAccessFile(new File(fileName), "rw");

            for(int i = lineLength; i<this.RawDataAsc.size(); i++) {
                String[] singleLine = this.RawDataAsc.get(i);
                fileWriter.write(singleLine[0].getBytes());
                fileWriter.write(csvSplitBy.getBytes());
                for (int j = i - lineLength+1; j <= i; j++) {
                    fileWriter.write(this.RawDataAsc.get(j)[1].getBytes());
                    if (j!=i) {
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


    protected void generateCsvFileDesc(String fileName, String delimiter) {
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

    public String getEndDate() {
        return endDate;
    }

    public String getBeginDate() {
        return beginDate;
    }


}
