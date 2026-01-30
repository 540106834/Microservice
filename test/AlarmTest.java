public class AlarmTest {

    public static void main(String[] args) {

        AlarmService alarm;

        alarm = new EmailAlarmService();
        alarm.send("CPU 使用率过高");

        alarm = new DingTalkAlarmService();
        alarm.send("磁盘剩余空间不足");
    }
}
