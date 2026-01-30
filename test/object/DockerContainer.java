public class DockerContainer {
    String name;
    String image;
    String running;
    
    public DockerContainer(String name, String image, String running){
        this.name = name;
        this.image = image;
        this.running = running;
    }

    public void start(){
        System.out.println("The Container: " + name + "\n" + "image: " + image +  "\n status is " + running);
    }

    void stop(){
        System.out.println("The Container: " + name + "\n image: " + image + "\n status is " + running );
    }
}
