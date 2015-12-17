package de.soco.stockmarket.controller;

import de.soco.stockmarket.service.DataService;
import de.soco.stockmarket.service.EnvService;

import org.apache.log4j.Logger;
import org.json.JSONArray;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


/**
 * Created by bthofrichter on 08.11.15.
 */
@RestController
@RequestMapping(value = "/stock")
public class AnnController implements ServletContextAware, ServletConfigAware {

    @Autowired
    private EnvService envService;

    @Autowired
    private DataService dataService;

    private static final Class<AnnController> applicationClass = AnnController.class;

    private static Logger logger = Logger.getLogger(applicationClass);


    private ServletContext servletContext;

    private ServletConfig config;

    @Override
    public void setServletConfig(final ServletConfig servletConfig) {
        this.config = servletConfig;
    }

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    @RequestMapping(value= "/data", method = RequestMethod.GET)
    public String data(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String collapse, @RequestParam String stock, @RequestParam String ann, @RequestParam Boolean saveNorm, @RequestParam Boolean saveClose ) {

            try {
                List<String> quandlData = new ArrayList<>();
                String format = envService.getProperty("quandl.data.dataset.format");
                String exclude_header = envService.getProperty("quandl.data.dataset.header");
                String order = envService.getProperty("quandl.data.dataset.order");
                String baseUrl = envService.getProperty("quandl.api.baseurl");
                String dataSet = envService.getProperty("quandl.dataset." + stock);
                String api_key = envService.getProperty("quandl.api.key");
                Integer periodLength = Integer.parseInt(envService.getProperty("format.period.length"));
                Boolean isDateIncluded = Boolean.parseBoolean(envService.getProperty("format.data.included"));


                try {
                if(startDate != null && endDate != null && collapse != null && stock  != null && ann != null) {
                    String https_url = baseUrl + dataSet + "." + format + "?order=" + order + "&exclude_column_names=" + exclude_header + "&start_date=" + startDate + "&end_date=" + endDate + "&collapse=" + collapse + "&api_key=" + api_key;

                    URL url;

                    url = new URL(https_url);
                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String input;

                    while ((input = br.readLine()) != null) {
                        quandlData.add(input);
                    }
                    br.close();

                    // formatting Data
                    List<List<String>> closeData = dataService.extractCloseData(quandlData, periodLength);
                    System.out.println("CLOSE:");
                    System.out.println(Arrays.toString(closeData.toArray()));

                    // Normalize Data
                    List<List<String>> normalizedData = dataService.normalize(closeData, isDateIncluded);

                    System.out.println("NOMRMALIZED:");
                    System.out.println(Arrays.toString(normalizedData.toArray()));

                    if(saveClose) {
                        dataService.saveData(closeData, dataSet, "");
                        logger.info("Close-Values-CSV-Data saved.");
                    }
                    if(saveNorm) {
                        dataService.saveData(normalizedData, dataSet, "normalized");
                        logger.info("Normalized CSV-Data saved.");
                    }

                    // Test with .nnet and add po, dist_vals
                    List<List<String>> testedData = dataService.testWithAnn(normalizedData, ann);
                    System.out.println("TESTED:");
                    System.out.println(Arrays.toString(testedData.toArray()));

                    // Denomralize
                    List<List<String>> denormData = dataService.deNormalize(testedData, isDateIncluded);

                    System.out.println("DENOMRMALIZED:");
                    System.out.println(Arrays.toString(denormData.toArray()));

                    List<List<String>> errorsAddedList = dataService.addErrorMessure(denormData);
                    System.out.println("DIST-MSE-Added:");
                    System.out.println(Arrays.toString(errorsAddedList.toArray()));

                    return dataService.parseResponseList(errorsAddedList);
                }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                  catch (Exception e) {
                 e.getMessage();
               }

        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    @RequestMapping(value= "/ann_names", method = RequestMethod.GET)
    public List<String> ann_names() {
        try {
            String media_source = envService.getProperty("media.source.base") + envService.getProperty("media.source.ann");
            List<String> result = new ArrayList<>();

            result = dataService.listFiles(media_source);

            return result;

        } catch (Exception e) {
            e.getMessage();
        }
        return Collections.emptyList();
    }

    @RequestMapping(value= "/datasets", method = RequestMethod.GET)
    public String datasets() {
        try {
            List<String> result = new ArrayList<>();
            JSONArray array = new JSONArray();
            array.put(envService.addProperty("quandl.dataset.name.dax"));
            array.put(envService.addProperty("quandl.dataset.name.nikkei_225"));
            array.put(envService.addProperty("quandl.dataset.name.djia"));

            return array.toString();

        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

}
