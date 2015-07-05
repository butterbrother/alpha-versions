// Arcs - дуги
import java.awt.*;
import java.awt.event.*;

public class Arcs extends Frame {
	private int nzY = 20;

	private Arcs() {
		super("Рисование дуг (Arc)");
		setSize(300,250);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawArc(10, 40+nzY, 70, 70, 0, 75);
		g.fillArc(100, 40+nzY, 70, 70, 0, 75);
		g.drawArc(10, 100+nzY, 70, 80, 0, 175);
		g.fillArc(100, 100+nzY, 70, 90, 0, 270);
		g.drawArc(200, 80+nzY, 80, 80, 0, 180);
	}

	public static void main(String args[]) {
		new Arcs();
	}
}
