package com.cracker.prometheus.use.type;

import io.prometheus.client.Gauge;

public class YourClass {

    static final Gauge INPROGRESS_REQUESTS = Gauge.build()
            .name("inprogress_requests")
            .help("Inprogress requests.")
            .labelNames("method")
            .register();

    void processRequest() {
        INPROGRESS_REQUESTS.inc();
        // Your code here.
        INPROGRESS_REQUESTS.dec();
    }
}
