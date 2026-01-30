
public abstract class BaseService {
    protected String name;
    protected int port;

    public BaseService(String name,int port){
        this.name = name;
        this.port = port;
    }

    public abstract void start();

    public void printInfo(){
        System.out.println("server: " + name + ",port: " + port);
    }
}
