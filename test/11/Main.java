public class Main {
    public static void main(String[] args) {

        Service s1 = new WebService();
        Service s2 = new DbService();

        s1.start();
        s2.start();
    }
}
