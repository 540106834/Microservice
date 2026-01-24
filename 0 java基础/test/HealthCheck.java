import java.time.LocalDateTime;

public class HealthCheck {

    public static String health() {
        return "{ \"status\": \"UP\", \"time\": \""
                + LocalDateTime.now() + "\" }";
    }

    public static void main(String[] args) {
        System.out.println(health());
    }
}
