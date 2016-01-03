package com.vnstreaming.dj;

import com.vnstreaming.dj.manager.FFmpegManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by linhnc on 12/31/15.
 */
public class App {
    static final Logger logger = LogManager.getLogger(App.class.getName());
    private static ApplicationContext applicationContext;

    public static void main(String[] args) throws InterruptedException{
        logger.debug("Start application");
        applicationContext = new ClassPathXmlApplicationContext("vnstreaming-dj.xml");
        FFmpegManager.getInstance().start();
        while (true) {
            Thread.sleep(1000);
        }
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}