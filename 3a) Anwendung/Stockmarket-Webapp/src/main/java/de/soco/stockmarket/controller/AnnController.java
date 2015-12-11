package de.soco.stockmarket.controller;

import de.soco.stockmarket.service.DataService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

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
    private Environment env;

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
    public List<String> data(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String collapse, @RequestParam String stock, @RequestParam String ann) {

            try {
                List<String> quandlData = new ArrayList<>();
                String format = env.getProperty("quandl.data.dataset.format");
                String exclude_header = env.getProperty("quandl.data.dataset.header");
                String order = env.getProperty("quandl.data.dataset.order");
                String baseUrl = env.getProperty("quandl.api.baseurl");
                String dataSet = env.getProperty("quandl.dataset." + stock);
                String api_key = env.getProperty("quandl.api.key");
                Integer periodLength = Integer.parseInt(env.getProperty("format.period.length"));


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

                    //set Quandl Data
                    dataService.setQdata(quandlData);
                    // formatting Data
                    List<List<String>> closeData = dataService.extractCloseData(periodLength);
                    // save formatted Data as csv
                    dataService.saveData(closeData, dataSet, "");
                    // Normalize Data
                    List<List<String>> normalizedData = dataService.normalize(closeData, true);
                    // save normalized Data as csv
                    dataService.saveData(normalizedData, dataSet, "normalized");
                    // Test with .nnet and add po
                    dataService.testWithAnn(normalizedData, ann);
                    // Denomralize
                    dataService.deNormalize();
                    // add dist_vals to Data
                    dataService.addDistVals();
                    // Add MSn
                    dataService.addMSE();
                    // return Data

                }
                return quandlData;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                  catch (Exception e) {
                 e.getMessage();
               }
///            AnnPerformer annPerformer = new AnnPerformer("resources/ann/4-09-1_Sigmoid_Bias.nnet", 4, 1);
// Load Excel Base File Date, _ _ _ _ ro

// Load Excel Base File Date, _ _ _ _ ro

        } catch (Exception e) {
            e.getMessage();
        }
        return Collections.emptyList();
    }

    @RequestMapping(value= "/ann_names", method = RequestMethod.GET)
    public List<String> ann_names() {
        try {
            String media_source = env.getProperty("media.source.ann");
            List<String> result = new ArrayList<>();

            result = listFiles(media_source);

            return result;

        } catch (Exception e) {
            e.getMessage();
        }
        return Collections.emptyList();
    }

    public List<String> listFiles(String directoryName){
        List<String> result = new ArrayList<>();

        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isFile()){
                result.add(file.getName());
            }
        }
        return result;
    }

    @RequestMapping(value= "/datasets", method = RequestMethod.GET)
    public String datasets() {
        try {
            List<String> result = new ArrayList<>();
            JSONArray array = new JSONArray();
            array.put(addProperty("quandl.dataset.name.dax"));
            array.put(addProperty("quandl.dataset.name.nikkei_225"));
            array.put(addProperty("quandl.dataset.name.djia"));

            return array.toString();

        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    private String addProperty(String propertyName) {
        try {
            JSONObject object = new JSONObject();
            String propStr = env.getProperty(propertyName);
            List<String> list = Arrays.asList(propStr.split(","));
            object.put( "name", list.get(0));
            object.put("value", list.get(1));
            return object.toString();
        } catch (Exception e) {
            System.out.print(e);
        }
        return "";
    }


//    private void print_content(HttpsURLConnection con){
//        if(con!=null){
//
//            try {
//
//                System.out.println("****** Content of the URL ********");
//                BufferedReader br =
//                        new BufferedReader(
//                                new InputStreamReader(con.getInputStream()));
//
//                String input;
//
//                while ((input = br.readLine()) != null){
//                    System.out.println(input);
//                }
//                br.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
}
