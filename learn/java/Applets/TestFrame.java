import java.awt.*;
import java.awt.event.*;

public class TestFrame {
	private static class TestWindow extends Frame {
		public TestWindow(String windowName) {
			super(windowName);

			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					setVisible(false);
					System.exit(0);
				}
			});

			setSize(250, 250);
			setVisible(true);
			repaint();
		}

		public void paint(Graphics g) {
			g.drawString("Test window", 10, 20);
		}
	}

	public static void main(String args[]) {
		TestWindow tst = new TestWindow("test window");
	}
}
