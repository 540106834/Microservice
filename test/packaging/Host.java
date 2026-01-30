
public class Host {
    public void startService(BaseService service){
        service.printInfo();
        service.start();
    }    
}
