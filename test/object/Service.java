public class Service {
    String name;
    Config config;

    public Service (String name, Config config){
        this.name = name;
        this.config = config;
    }

    void start(){
        System.out.println("service: " + name + "use the config" + config.profile + " start");
    }
}
