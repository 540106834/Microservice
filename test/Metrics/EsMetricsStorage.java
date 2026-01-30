public class EsMetricsStorage implements MetricsStorage {

    @Override
    public void save(Metric metric) {
        System.out.println(
            " 存入 ES: " + metric.getName() + " = " + metric.getValue()
        );
    }
}
