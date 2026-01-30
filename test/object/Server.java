public class Server {
    String hostname;
    String ip;
    String status;

    void start(){
        status = "RUNNING";
        System.out.println(hostname + "启动成功");    
    }
    void stop(){
        status = "Stopped";
        System.out.println(hostname + "已停止");
    }
    void printInfo(){
        System.out.println("主机名：" + hostname + ", IP: " + ip + ", 状态：" + status);
    }  
}

