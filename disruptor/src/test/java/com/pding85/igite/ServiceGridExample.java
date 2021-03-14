package com.pding85.igite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.internal.util.IgniteUtils;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ServiceGridExample {
    public static void main(String[] args) throws Exception {
        URLClassLoader urlClassLoader = ((URLClassLoader) (IgniteUtils.class.getClassLoader()));
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
        add.setAccessible(true);
        urlClassLoader.loadClass("com.pding85.igite.WeatherService");
        urlClassLoader.loadClass("com.pding85.igite.WeatherServiceImpl");

        try (Ignite ignite = Ignition.start("F:\\program\\java\\code\\apache-ignite-2.9.1-bin\\apache-ignite-2.9.1-bin\\examples\\config/example-ignite.xml")) {
            // Deploying a single instance of the Weather Service
            // in the whole cluster.


            ignite.services().deployClusterSingleton("WeatherService",
                    new WeatherServiceImpl());
            // Requesting current weather for London.
            WeatherService service = ignite.services().service("WeatherService");
            String forecast = service.getCurrentTemperature("London", "UK");
            System.out.println("Weather forecast in London:" + forecast);
        }
    }
}