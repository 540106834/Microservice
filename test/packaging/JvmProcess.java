public class JvmProcess {
    private int memory;
    private boolean running;

    public JvmProcess(int memory){
        setMemory(memory);
    }

    public void setMemory(int memory){
        if(memory <= 0){
            throw new IllegalArgumentException("The memory must be greater than 0 ");
        }
        this.memory = memory;
    }

    public int getMemory(){
        return memory;
    }

    public void start(){
        running = true;
        System.out.println("JVM starting, get the memory: " + memory + "MB");
    }
}
