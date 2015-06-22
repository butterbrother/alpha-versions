import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by user on 21.05.2015.
 */
public class propershow {
    public static void main(String args[]) {
        // Если таки ничего не указано
        if (args.length < 1) {
            System.out.println("Показываем properties с точки зрения java");
            System.out.println("В качестве параметра указываем .properties-файл");
            System.exit(0);
        }
        // Если указано
        try {
            Properties readedProps = new Properties();
            readedProps.load(new FileInputStream(new File(args[0])));
            System.out.println("Список:");
            for (Map.Entry<Object,Object> item: readedProps.entrySet()) {
                System.out.println(item.getKey().toString() + "=" + item.getValue());
            }
        } catch (Exception e) {
            System.err.println("Гы! Попробуй ещё разок, сына!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
