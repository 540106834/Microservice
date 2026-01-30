public class MemoryMetricsCollector implements MetricsCollector {

    @Override
    public Metric collect() {
        double memoryUsage = Math.random() * 100;
        return new Metric("memory.usage", memoryUsage);
    }
}
