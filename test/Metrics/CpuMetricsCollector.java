public class CpuMetricsCollector implements MetricsCollector {

    @Override
    public Metric collect() {
        double cpuUsage = Math.random() * 100; // 模拟
        return new Metric("cpu.usage", cpuUsage);
    }
}
