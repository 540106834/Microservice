import java.time.LocalTime;

public class Demo01 {

    public String now() {
        return LocalTime.now().toString();
    }

    public static void main(String[] args) {
        Demo01 timeService = new Demo01();
        System.out.println(timeService.now());
    }
}
