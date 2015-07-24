import java.awt.*;
import java.awt.event.*;

public class ColorDemo extends Frame {
	private int nzY=20;
	public ColorDemo() {
		super("Демонстрация цветов");
		setSize(300,200 + nzY);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		Color c1 = new Color(255, 100, 100);
		Color c2 = new Color(100, 255, 100);
		Color c3 = new Color(100, 100, 255);

		g.setColor(c1);
		g.drawLine(0, 0+nzY,	100,100);
		g.drawLine(0, 100+nzY,	100,0);

		g.setColor(c2);
		g.drawLine(40, 25+nzY,	250, 180);
		g.drawLine(75, 90+nzY,	400, 400);

		g.setColor(c3);
		g.drawLine(20, 150+nzY,	400, 40);
		g.drawLine(5, 290+nzY,	80, 19);

		g.setColor(Color.red);
		g.drawOval(10, 10+nzY,	50, 50);
		g.fillOval(70, 90+nzY,	140, 100);

		g.setColor(Color.blue);
		g.drawOval(190, 10+nzY,	90, 30);
		g.drawRect(10, 10+nzY,	60, 50);

		g.setColor(Color.cyan);
		g.fillRect(100, 10+nzY,	60, 50);
		g.drawRoundRect(190, 10+nzY,	60, 50,		15, 15);
	}

	public static void main(String args[]) {
		new ColorDemo();
	}
}
