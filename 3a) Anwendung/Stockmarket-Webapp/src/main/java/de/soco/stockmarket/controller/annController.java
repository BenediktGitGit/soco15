package de.soco.stockmarket.controller;

import com.fasterxml.jackson.databind.Module;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;



/**
 * Created by bthofrichter on 08.11.15.
 */
@RestController
@RequestMapping(value = "/stock")
public class AnnController implements ServletContextAware, ServletConfigAware {

    @Autowired
    private Environment env;

//    @Autowired
//    ServletContext servletContext;

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
    public String data(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String collapse, @RequestParam String stock, @RequestParam String ann) {
        try {
            try {
                String baseUrl = env.getProperty("source.baseurl");
//                String yahooDax = env.getProperty("source.dataset.dax");
                String api_key = env.getProperty("quandl.api.key");


                String https_url = baseUrl + stock + "?start_date=" + startDate + "&end_date=" + endDate + "&collapse=" + collapse + "&api_key=" + api_key;
                URL url;
                try {

                    url = new URL(https_url);
                    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return "";
            } catch (Exception e) {
                e.getMessage();
            }
///            AnnPerformer annPerformer = new AnnPerformer("resources/ann/4-09-1_Sigmoid_Bias.nnet", 4, 1);
// Load Excel Base File Date, _ _ _ _ ro

// Load Excel Base File Date, _ _ _ _ ro


            return "";

        } catch (Exception e) {
            e.getMessage();
        }
        return "hello";
    }

    @RequestMapping(value= "/ann_names", method = RequestMethod.GET)
    public List<String> ann_names() {
        try {
            String media_source = env.getProperty("media.source");
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
    public List<String> datasets() {
        try {
            List<String> result = new ArrayList<>();
            result.add(env.getProperty("quandl.dataset.dax"));

            return result;

        } catch (Exception e) {
            e.getMessage();
        }
        return Collections.emptyList();
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
