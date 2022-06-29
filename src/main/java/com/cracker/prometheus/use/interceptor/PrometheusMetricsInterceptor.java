package com.cracker.prometheus.use.interceptor;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrometheusMetricsInterceptor extends HandlerInterceptorAdapter {

    static final Counter REQUEST_COUNTER = Counter.build()
            .name("io_namespace_http_requests_total").labelNames("path", "method", "code")
            .help("Total requests.").register();

    static final Gauge INPROGRESS_REQUESTS = Gauge.build()
            .name("io_namespace_http_inprogress_requests").labelNames("path", "method")
            .help("Inprogress requests.").register();

    static final Histogram REQUEST_LATENCY_HISTOGRAM = Histogram.build().labelNames("path", "method", "code")
            .name("io_namespace_http_requests_latency_seconds_histogram").help("Request latency in seconds.")
            .register();

    private Histogram.Timer histogramRequestTimer;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        histogramRequestTimer = REQUEST_LATENCY_HISTOGRAM.labels(requestURI, method, String.valueOf(status)).startTimer();
        INPROGRESS_REQUESTS.labels(requestURI, method).inc();
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        histogramRequestTimer.observeDuration();
        REQUEST_COUNTER.labels(requestURI, method, String.valueOf(status)).inc();
        INPROGRESS_REQUESTS.labels(requestURI, method).dec();
        super.afterCompletion(request, response, handler, ex);
    }
}
