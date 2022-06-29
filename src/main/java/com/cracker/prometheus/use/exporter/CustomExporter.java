package com.cracker.prometheus.use.exporter;

import com.cracker.prometheus.use.collector.MyCustomCollector;
import com.cracker.prometheus.use.collector.MyCustomCollector2;
import com.cracker.prometheus.use.integration.PushGatewayIntegration;
import com.cracker.prometheus.use.type.YourClass;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;
import io.prometheus.client.exporter.HTTPServer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CustomExporter extends Collector {

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();
        GaugeMetricFamily labeledGauge =
                new GaugeMetricFamily("io_namespace_custom_metrics", "custom metrics", Collections.singletonList("labelname"));
        labeledGauge.addMetric(Collections.singletonList("labelvalue"), 1);
        mfs.add(labeledGauge);
        return mfs;
    }

    public static void main(String[] args) throws IOException {
        new MyCustomCollector().register();
        new MyCustomCollector2().register();
        new YourClass();
        //DefaultExports.initialize();
        new PushGatewayIntegration().push();
        HTTPServer server = new HTTPServer(1234);
    }
}
