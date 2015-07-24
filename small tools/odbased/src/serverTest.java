import org.butterbrother.odbased.ClientSocket;
import org.butterbrother.odbased.DaemonSocket;

import java.io.IOException;

/**
 * Created by obobuh on 24.07.2015.
 */
public class serverTest {
    public static void main(String args[]) {
        try (DaemonSocket ds = new DaemonSocket()) {
            try (ClientSocket cs = new ClientSocket()) {
                cs.sendRequest("Test message");
                while (! cs.haveResponse()) {
                    Thread.sleep(100);
                }
                System.out.println(cs.getResponse());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
