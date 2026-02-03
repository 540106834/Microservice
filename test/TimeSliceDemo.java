public class TimeSliceDemo {
    public static void main(String[] args) {

        new Thread(() -> {
            while (true) {
                System.out.println("A");
            }
        }).start();

        new Thread(() -> {
            while (true) {
                System.out.println("B");
            }
        }).start();
    }
}
