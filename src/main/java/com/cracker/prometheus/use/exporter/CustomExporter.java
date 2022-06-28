package com.cracker.prometheus.use.exporter;

import com.cracker.prometheus.use.type.YourClass;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public class CustomExporter {

    public static void main(String[] args) throws IOException {
        //new MyCustomCollector().register();
        //new MyCustomCollector2().register();
        new YourClass();
        //DefaultExports.initialize();
        HTTPServer server = new HTTPServer(1234);
    }
}
