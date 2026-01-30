public class User {
    private String username;
    private int age;

    public void setUsername(String username){
        if (username == null || username.isEmpty()){
            System.out.println(" Username is invalid.");
        }
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setAge(int age){
        if (age < 0 || age > 150){
            System.out.println("Age is invalid");
        }
        this.age = age;
    }

    public int getAge(){
        return age;
    }

}
