package org.paytm;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication  //marks the starting point of the spring application
public class MainApp {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    //ALL > TRACE > DEBUG > INFO > WARN > ERROR > FATAL > OF
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);

        System.out.println("Application starts...");
        log.debug("Testing starts...");
    }
}