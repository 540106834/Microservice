public class Demo01 {

    public static void main(String[] args) {

        Integer a = 127;
        Integer b = 127;

        Integer c = 128;
        Integer d = 128;

        System.out.println(a == b); // true
        System.out.println(c == d); // false

        System.out.println(c.equals(d)); // true
    }
}