package de.soco.stockmarket;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by bthofrichter on 08.11.15.
 */


@PropertySource("classpath:app.properties")
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static final Class<Application> applicationClass = Application.class;

    private static Logger logger = Logger.getLogger(applicationClass);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        logger.info("Application starting ... ");
    }

}
