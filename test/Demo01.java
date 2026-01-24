class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

public class Demo01 {
    public static void main(String[] args) {
        Person person = new Person("Alice", 30);
        person.displayInfo();
    }
}