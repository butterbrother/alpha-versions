import java.awt.*;
import java.applet.*;
import java.net.*;

public class ACDemo extends Applet {
	public void start() {
		AppletContext ac = getAppletContext();
		URL url = getCodeBase();

		try {
			ac.showDocument(new URL(url + "Test.html"));
		} catch (MalformedURLException e) {
			showStatus("URL not found");
		}
	}
}
