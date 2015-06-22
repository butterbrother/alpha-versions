import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by user on 21.05.2015.
 */
public class propershow {
    public static void main(String args[]) {
        // ���� ���� ������ �� �������
        if (args.length < 1) {
            System.out.println("���������� properties � ����� ������ java");
            System.out.println("� �������� ��������� ��������� .properties-����");
            System.exit(0);
        }
        // ���� �������
        try {
            Properties readedProps = new Properties();
            readedProps.load(new FileInputStream(new File(args[0])));
            System.out.println("������:");
            for (Map.Entry<Object,Object> item: readedProps.entrySet()) {
                System.out.println(item.getKey().toString() + "=" + item.getValue());
            }
        } catch (Exception e) {
            System.err.println("��! �������� ��� �����, ����!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
