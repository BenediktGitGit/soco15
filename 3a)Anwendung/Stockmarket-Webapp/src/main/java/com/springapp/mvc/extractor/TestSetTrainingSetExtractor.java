package com.springapp.mvc.extractor;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.List;

/**
 * Created by Benedikt Hofrichter on 31.10.15.
 *
 *
 */
public class TestSetTrainingSetExtractor extends Extractor {

    @Autowired
    ExtractorReport extractorReport;

    public TestSetTrainingSetExtractor(String filename, int lineLength, String csvSplitBy) {
        super(filename, lineLength, csvSplitBy);
        setColSignatur(new int[] {0,1, 2, 3, 4});
    }

    @Override
    protected void createFileName(String setType) {
        this.filename = "StockMarket" + "-" + this.getBeginDate() + "_" + this.getEndDate() + "-" + setType + ".csv";
        this.targetLocation = this.prefix + this.filename;
    }

    @Override
    protected List<String[]> getStockDataRawAsc() {
        return super.getStockDataRawAsc();
    }

}




