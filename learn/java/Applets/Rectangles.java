import java.awt.*;
import java.awt.event.*;

public class Rectangles extends Frame {
	public static void main(String args[]) {
		new Rectangles();
	}

	public Rectangles() {
		super("Rectangles");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		setSize(300,200);
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawRect(10, 10, 60, 50);
		g.fillRect(100, 10, 60, 50);
		g.drawRoundRect(190, 10, 60, 50, 15, 15);
		g.fillRoundRect(70, 90, 140, 100, 30, 40);
	}
}
