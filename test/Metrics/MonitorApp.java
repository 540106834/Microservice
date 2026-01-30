import java.util.Arrays;

public class MonitorApp {

    public static void main(String[] args) {

        MetricsCollector cpu = new CpuMetricsCollector();
        MetricsCollector mem = new MemoryMetricsCollector();

        MetricsStorage storage = new EsMetricsStorage();
        // MetricsStorage storage = new MySqlMetricsStorage();
        // MetricsStorage storage = new PrometheusMetricsStorage();

        MetricsScheduler scheduler =
            new MetricsScheduler(Arrays.asList(cpu, mem), storage);

        scheduler.runOnce();
    }
}
