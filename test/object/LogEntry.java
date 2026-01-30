

public class LogEntry {
    String level;
    String message;

    public LogEntry(String level,String message){
        this.level = level;
        this.message = message;
    }

    void print(){
        System.out.println("[" + level + "]" + message);
    }
}
