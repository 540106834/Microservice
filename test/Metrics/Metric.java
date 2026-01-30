public class Metric {

    private String name;
    private double value;
    private long timestamp;

    public Metric(String name, double value) {
        this.name = name;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    // getter / setter 省略

}
