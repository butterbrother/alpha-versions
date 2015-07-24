import java.awt.*;
import java.awt.event.*;

public class Polygons extends Frame {
	private int nzY=20, num = 5;
	private int xpoints[] = {30, 200, 30, 200, 30};
	private int ypoints[] = {30+nzY, 30+nzY, 200+nzY, 200+nzY, 30+nzY};

	private Polygons() {
		super("Рисование многоугольников");
		setSize(230, 240);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawPolygon(xpoints, ypoints, num);
	}

	public static void main(String args[]) {
		new Polygons();
	}
}
