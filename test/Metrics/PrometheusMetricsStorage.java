public class PrometheusMetricsStorage implements MetricsStorage {

    @Override
    public void save(Metric metric) {
        System.out.println(
            " 推送 Prometheus: " + metric.getName() + " " + metric.getValue()
        );
    }
}
