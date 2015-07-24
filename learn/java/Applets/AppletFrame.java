import java.awt.*;
import java.awt.event.*;
import java.applet.*;

class SampleFrame extends Frame {
	SampleFrame(String title) {
		super(title);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				setVisible(false);
			}
		});
	}

	public void paint(Graphics g) {
		g.drawString("This is in frame window", 10, 40);
	}
}

public class AppletFrame extends Applet {
	Frame f;

	public void init() {
		f = new SampleFrame("A Frame window");
		f.setSize(250, 250);
		f.setVisible(true);
	}

	public void start() {
		f.setVisible(true);
	}

	public void stop() {
		f.setVisible(false);
	}

	public void paint(Graphics g) {
		g.drawString("This is applet window", 10, 20);
	}
}
