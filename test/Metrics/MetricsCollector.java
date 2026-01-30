package test.Metrics;
public interface MetricsCollector {

    /**
     * 采集监控数据
     */
    Metric collect();
}
