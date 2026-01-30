public class Main {
    public static void main(String[] args) {
        JvmProcess jp = new JvmProcess(20);
        System.out.println("the memory is :" + jp.getMemory());
        jp.start();
    }
}
