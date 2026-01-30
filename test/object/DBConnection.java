public class DBConnection {
    String url;
    String username;
    String password;

    public DBConnection(String url, String username,String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    void connect(){
        System.out.println("链接数据库： " + url + ", 用户：" + username);
    }
}
