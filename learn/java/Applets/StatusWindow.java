import java.awt.*;
import java.applet.*;

public class StatusWindow extends Applet {
	public void init() {
		setBackground(Color.green);
	}

	public void paint(Graphics g) {
		g.drawString("This is in the applet window.", 10, 20);
		showStatus("This is who in the status window.");
	}
}
