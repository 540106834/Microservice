public class MySqlMetricsStorage implements MetricsStorage {

    @Override
    public void save(Metric metric) {
        System.out.println(
            " 存入 MySQL: " + metric.getName() + " = " + metric.getValue()
        );
    }
}
