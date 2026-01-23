public class Demo01 {

    static boolean checkServer(int cpu) {
        return cpu < 80;
    }

    public static void main(String[] args) {
        int[] servers = {45, 66, 90, 30};

        for (int cpu : servers) {
            if (checkServer(cpu)) {
                System.out.println(cpu + "% OK");
            } else {
                System.out.println(cpu + "% ALERT");
            }
        }
    }
}