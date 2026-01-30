import java.util.*;

public class MetricsScheduler {

    private List<MetricsCollector> collectors;
    private MetricsStorage storage;

    public MetricsScheduler(List<MetricsCollector> collectors,
                            MetricsStorage storage) {
        this.collectors = collectors;
        this.storage = storage;
    }

    public void runOnce() {
        for (MetricsCollector collector : collectors) {
            Metric metric = collector.collect();
            storage.save(metric);
        }
    }
}
