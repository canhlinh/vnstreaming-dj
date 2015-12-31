package com.vnstreaming.dj;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by linhnc on 12/31/15.
 */
public class App {
    static final Logger logger = LogManager.getLogger(App.class.getName());
    public static void main(String[] args) throws InterruptedException{
        logger.debug("Start application");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("vnstreaming-dj.xml");
        while (true) {
            Thread.sleep(1000);
        }
    }
}