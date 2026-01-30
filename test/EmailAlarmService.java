public class EmailAlarmService implements AlarmService {

    @Override
    public void send(String message) {
        System.out.println("📧 发送邮件告警：" + message);
    }
}
