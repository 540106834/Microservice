
public class JobService extends BaseService {
    public JobService(String name,int port){
        super(name,port);
    }

    @Override
    public void start(){
        System.out.println("start cronjob service: " + name);
    }
}
