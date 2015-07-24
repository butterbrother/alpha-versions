import java.awt.*;
import java.awt.event.*;

public class Lines extends Frame {
	public Lines(String header) {
		super(header);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		setSize(300, 200);
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawLine(0,0, 100,100);
		g.drawLine(0,100, 100,0);
		g.drawLine(40,24, 250,180);
		g.drawLine(75,90, 400,400);
		g.drawLine(20,150, 400,40);
		g.drawLine(5,290, 80,19);
	}

	public static void main(String args[]) {
		new Lines("Lines");
	}
}
