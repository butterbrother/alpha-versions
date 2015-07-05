import java.awt.*;
import java.awt.event.*;

public class Ellipses extends Frame {
	public Ellipses() {
		super("Ellipses");
		setSize(300, 250);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void paint(Graphics g) {
		g.drawOval(10,40, 50,50);
		g.fillOval(100,40, 75,50);
		g.drawOval(190,40, 90,30);
		g.fillOval(70,90, 140,100);
	}

	public static void main(String args[]) {
		new Ellipses();
	}
}
