import org.ini4j.Ini;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * <EMPTY HEADER, PLEASE EDIT>
 * Created by user on 08.01.15.
 */
public class CreateAndSave {
    public static CreateAndSave genInstance() {
        return new CreateAndSave();
    }
    public static void main(String args[]) {
        Ini sampleIni = new Ini();
        Ini.Section sect = sampleIni.add("test");
        sect.add("Param 1", 4);
        sect.add("Param 2", 2.145);
        sect.remove("Param 2");
        sect.add("Param 2", 3.456);
        try {
            sampleIni.store(new OutputStreamWriter(new FileOutputStream("test.ini"), "UTF-8"));
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        // now read
        Ini readedIni = null;
        try {
            readedIni = new Ini(new InputStreamReader(new FileInputStream("test.ini"), "UTF-8"));
            //readedIni.load();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        if (readedIni != null) {
            if (readedIni.containsKey("test")) {
                Ini.Section rs = readedIni.get("test");
                int param_1 = rs.containsKey("Param 1") ? rs.get("Param 1", Integer.class) : 0;
                double param_2 = rs.containsKey("Param 2") ? rs.get("Param 2", Double.class) : 0.0;
                System.out.println("Param 1 - " + param_1);
                System.out.println("Param 2 - " + param_2);
            }
        }
    }
}
