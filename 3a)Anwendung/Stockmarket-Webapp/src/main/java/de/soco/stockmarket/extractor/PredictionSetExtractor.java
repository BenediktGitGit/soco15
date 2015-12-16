package de.soco.stockmarket.extractor;

import java.util.List;
import java.lang.*;

/**
 * Created by Benedikt Hofrichter on 31.10.15.
 *
 *
 */


public class PredictionSetExtractor extends Extractor {


    public PredictionSetExtractor(String filename, int lineLength, String csvSplitBy) {
        super(filename, lineLength, csvSplitBy);
        setColSignatur(new int[] {1, 2, 3, 4});
    }

    @Override
    protected void createFileName(String setType) {
        this.filename = this.getBeginDate() + "_" + this.getEndDate() + "-" + setType + ".csv";
        this.targetLocation = this.prefix +  this.filename;
    }

    @Override
    protected List<String[]> getStockDataRawAsc() {
        return super.getStockDataRawAsc();
    }




//    @Override
//    protected void getStockDataRawAsc() {
//        List<String[]> tmp = new ArrayList<String[]>();
//        tmp = this.getStockDataRawDesc();
//        this.RawDataAsc = new ArrayList<String[]>(tmp.size());
//        for (int i = tmp.size()-1; i >= 0 ; i--) {
//            this.RawDataAsc.add(tmp.get(i));
//        }
//        setBeginDate(this.RawDataAsc.get(lineLength)[0]);
//        setEndDate(this.RawDataAsc.get(this.RawDataAsc.size() - 1)[0]);
//        setColumnLength(this.RawDataAsc.size());
//        getMaxValofCol();
//    }
//
//    @Override
//    protected void generateCsvFileAsc(String fileName, String delimiter) {
//        try {
//            String[] lineValueSet = new String[this.lineLength];
//            RandomAccessFile fileWriter = new RandomAccessFile(new File(fileName), "rw");
//
//            for(int i = lineLength; i<this.RawDataAsc.size(); i++) {
//                String[] singleLine = this.RawDataAsc.get(i);
//                fileWriter.write(singleLine[0].getBytes());
//                fileWriter.write(csvSplitBy.getBytes());
//                for (int j = i - lineLength+1; j <= i; j++) {
//                    fileWriter.write(this.RawDataAsc.get(j)[1].getBytes());
//                    if (j!=i) {
//                        fileWriter.write(csvSplitBy.getBytes());
//                    }
//                }
//                fileWriter.write("\n".getBytes());
//            }
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

//
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
//                        line = br.readLine();
//                        if (line != null) {
//                            lineValueSet[i] = line;
//                            counter++;
//                        }
//                }
//
//                // Nur vollständige Zeilen in csv übernehmen
//                if (counter == this.lineLength) {
//                    String[] singleLine = lineValueSet[0].split(csvSplitBy);
//                        fileWriter.append(singleLine[0]);
//                        fileWriter.append(",");
//                    for (int i = lineLength - 1; i >= 0; i--) {
//                        singleLine = lineValueSet[i].split(csvSplitBy);
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


}
