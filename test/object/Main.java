public class Main {
    public static void main(String[] args){
        DockerContainer dockerContainer = new DockerContainer("nacos", "v1.2.3", "on");
        dockerContainer.start();
        dockerContainer.running = "down";
        dockerContainer.stop();       
    }
}