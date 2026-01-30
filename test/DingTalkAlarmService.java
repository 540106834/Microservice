public class DingTalkAlarmService implements AlarmService {

    @Override
    public void send(String message) {
        System.out.println(" 发送钉钉告警：" + message);
    }
}
