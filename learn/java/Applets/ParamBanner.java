import java.awt.*;
import java.applet.*;

public class ParamBanner extends Applet implements Runnable {
	String msg;
	int state;
	volatile boolean stopFlag;

	public void init() {
		setBackground(Color.black);
		setBackground(Color.white);
	}

	public void start() {
		msg = getParameter("message");
		if (msg == null) msg = "Message not found";
		msg = " " + msg;
		stopFlag = false;
		new Thread(this).start();
	}

	public void run() {
		for (;;) {
			try {
				repaint();
				Thread.sleep(250);
				if (stopFlag)
					break;
			} catch (InterruptedException e) {
				stopFlag = true;
			}
		}
	}

	public void stop() {
		stopFlag = true;
	}

	public void paint(Graphics g) {
		msg = msg.substring(1, msg.length()) + msg.charAt(0);
		g.drawString(msg, 30, 50);
	}
}
