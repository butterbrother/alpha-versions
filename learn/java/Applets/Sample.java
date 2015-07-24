import java.awt.*;
import java.applet.*;

public class Sample extends Applet {
	StringBuilder msg;

	public void init() {
		msg = new StringBuilder();
		msg.append("Inside init() --");
		setBackground(Color.cyan);
		setForeground(Color.red);
	}

	public void start() {
		msg.append(" Inside start() --");
	}

	public void paint(Graphics g) {
		msg.append(" Inside paint().");
		g.drawString(msg.toString(), 10, 30);
	}
}
