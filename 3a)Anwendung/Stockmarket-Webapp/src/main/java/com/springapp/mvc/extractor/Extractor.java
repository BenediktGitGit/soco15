package com.springapp.mvc.extractor;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import org.neuroph.core.data.DataSet;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bthofrichter on 04.11.15.
 */
public abstract class Extractor {


    private static final Class<Extractor> serviceClass = Extractor.class;

    private static Logger logger = Logger.getLogger(serviceClass);

    protected String csvLine;

    protected String prefix;

    protected List<String[]> RawDataAsc;

    protected List<String[]> dataStructure;

    protected List<String[]> dataSubSet;

    protected List<String> dateList;

    protected double daxMax;

    protected String filename;

    protected String targetLocation;

    protected int columnLength;

    protected String beginDate;

    protected String endDate;

    protected BufferedReader br;

    protected String line;

    protected String csvSplitBy;

    protected int lineLength;

    protected int[] colSignatur;

    public Extractor(String filename, int lineLength, String csvSplitBy) {
        this.csvLine = "resources/source/YAHOO-INDEX_GDAXI_RAW.csv";
        this.prefix = "resources/analyzed/";
        this.filename = filename;
        this.lineLength = lineLength;
        this.csvSplitBy = csvSplitBy;
        this.beginDate = "";
        this.endDate = "";
    }

    protected void createFileName(String setType) {  }

    public void extract() {

        RawDataAsc = getStockDataRawAsc(); // Daten aus Datei in Liste lesen
        dataStructure = getDataStructure(getRawDataAsc()); // CSV-Daten in Liste einlesen
        dateList = getRawDateList(getDataStructure()); // Dates in Liste extrahieren
        daxMax = getMaxValofCol(RawDataAsc, 1); // Maximum Daxwert extrahieren
        dataSubSet = getDataStructureSubSet(getDataStructure(), colSignatur); // Untermenge in Liste formatiert extrahieren

        createFileName(this.filename);
        System.out.print("creating file: " + this.targetLocation + " . . .");

        generateCsvFile(this.targetLocation, csvSplitBy, getDataSubSet()); // Liste aus Untermenge als CSV schreiben
        System.out.print("done.\n");

    }

    protected void generateCsvFile (String fileName, String delimiter, List<String[]> list) {
        try {
            RandomAccessFile fileWriter = new RandomAccessFile(new File(fileName), "rw");

            String[] lineValueSet = new String[this.lineLength];
            for(int i = 0; i < list.size(); i++) {
                String[] singleLine = list.get(i);
                for (int j = 0; j <= singleLine.length; j++) {
                    fileWriter.write(singleLine[j].getBytes());
                    if (j!=i) {
                        fileWriter.write(csvSplitBy.getBytes());
                    }
                }
                fileWriter.write("\n".getBytes());
            }
        } catch (Exception e) {
            logger.error("generateCsvFileException in " + serviceClass + e);
        }
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

    protected List<String[]> getStockDataRawAsc() {
        try {
            List<String[]> tmp = this.getStockDataRawDesc();
            List<String[]> list = new ArrayList<String[]>();
            for (int i = tmp.size() - 1; i >= 0; i--) {
                list.add(tmp.get(i));
            }
            setBeginDate(list.get(lineLength)[0]);
            setEndDate(list.get(list.size() - 1)[0]);
            setColumnLength(list.size());
            return list;
        }
        catch (Exception e) {
            logger.error("getStockDataRawAscException in " + serviceClass + e);
        }
        return Collections.emptyList();
    }


    protected List<String[]> getDataStructure (List<String[]> data) {
        try {
        List<String[]> result = new ArrayList<String[]>();
        String[] line = new String[lineLength];

        for(int i = lineLength; i< data.size(); i++) {
            String[] singleLine = data.get(i);

            for (int j = i - lineLength; j <= i; j++) {
                line[i] = data.get(j)[1];
            }
            result.add(line);
        }
        return result;
        }
        catch (Exception e) {
         logger.error("getDataStructureException in " + serviceClass + e);
         }
        return Collections.emptyList();
    }

    protected List<String[]> getDataStructureSubSet(List<String[]> list, int[] cols) {
        List<String[]> result = new ArrayList<String[]>();
        String[] line = new String[cols.length];

        for(int i = 0; i < list.size(); i++) {
            String[] singleLine = list.get(i);

            for(int j = 0; j < cols.length; i++) {
                line[j] = singleLine[cols[j]];
            }
            result.add(line);
        }
        return result;
    }

    protected List<String> getRawDateList(List<String[]> list) {
        try {
            List<String> result = new ArrayList<String>();
            for(String s[] : list) {
                result.add(s[0]);
            }
            return result;
        } catch (NullPointerException ne) {
            logger.error("emptyRawDataResultListException in " + serviceClass);
        }
        catch (Exception e) {
            logger.error("getRawDateListException in " + serviceClass);
        }
        return Collections.emptyList();
    }

    public double getMaxValofCol(List<String[]> list, int colNr) {
        double result = 0d;
        for(int i = 1; i < list.size(); i++) {
            double cur = Double.parseDouble(list.get(i)[colNr]);
            if (cur > result) {
                result = cur;
            }
        }
        return result;
    }

    protected DataSet normalize(DataSet trainingSet, Double factor, Double minLevel) {
//        int length = valuesRow.length;
//        if (length < lineLength) {
//            System.out.println("valuesRow.length < " + lineLength);
//            return null;
//        }
//        String[][] s = new String[lineLength][1];
//        double[] d = new double[lineLength];
//        try {
//            for (int i = 0; i < lineLength; i++) {
//                for(int j = 0; j < valuesRow.length; j++) {
//                    s[i] = valuesRow[j].split(",");
//                }
//                d[i] = (Double.parseDouble(s[i][0]) - minLevel) / this.daxMax;
//            }
//            trainingSet.addElement(new SupervisedTrainingElement(d, new double[]{d[lineLength-1]}));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
        return trainingSet;
    }


    public String getCsvLine() {
        return csvLine;
    }

    public void setCsvLine(String csvLine) {
        this.csvLine = csvLine;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String[]> getRawDataAsc() {
        return RawDataAsc;
    }

    public void setRawDataAsc(List<String[]> rawDataAsc) {
        RawDataAsc = rawDataAsc;
    }

    public double getDaxMax() {
        return daxMax;
    }

    public void setDaxMax(double daxMax) {
        this.daxMax = daxMax;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCsvSplitBy() {
        return csvSplitBy;
    }

    public void setCsvSplitBy(String csvSplitBy) {
        this.csvSplitBy = csvSplitBy;
    }

    public int getLineLength() {
        return lineLength;
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public void setColSignatur (int[] cols) {
        this.colSignatur = cols;
    }

    public List<String[]> getDataSubSet() {
        return dataSubSet;
    }

    public void setDataSubSet(List<String[]> dataSubSet) {
        this.dataSubSet = dataSubSet;
    }

    public List<String[]> getDataStructure() {
        return dataStructure;
    }

    public void setDataStructure(List<String[]> dataStructure) {
        this.dataStructure = dataStructure;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }
}
