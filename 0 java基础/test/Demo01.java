public class Demo01 {

    static void api() {
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        api();
        long end = System.currentTimeMillis();

        System.out.println("cost = " + (end - start) + " ms");
    }
}