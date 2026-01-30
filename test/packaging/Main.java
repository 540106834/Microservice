public class Main {
    public static void main(String[] args) {
        Host host = new Host();

        BaseService web = new WebService("gateway",8080);
        BaseService job = new JobService("sync-job",9000);

        host.startService(web);
        host.startService(job);
    }
}
