public class WebService extends BaseService{
    public WebService(String name,int port){
        super(name,port);
    }

    @Override
    public void start(){
        System.out.println("starting Web service" + name);
    }
}