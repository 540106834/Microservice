class Person {
    String name;
    int age;

    void sayHello() {
        System.out.println("你好，我叫 " + name);
    }
}

public class Demo {
    public static void main(String[] args) {

        Person p = new Person(); // 造对象
        p.name = "张三"; // 设置属性
        p.age = 18;

        p.sayHello(); // 调用方法
    }
}
