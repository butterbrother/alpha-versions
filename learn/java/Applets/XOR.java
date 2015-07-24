import java.awt.*;
import java.awt.event.*;

public class XOR extends Frame {
	int chsX=100, chsY=100;

	public static void main(String args[]) {
		new XOR();
	}

	public XOR() {
		super("XOR");
		setSize(400, 200);
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent me) {
				int x = me.getX();
				int y = me.getY();
				chsX = x-10;
				chsY = y-10;
				repaint();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawLine(0,0,	100,100);
		g.drawLine(0,100,	100, 0);
		g.setColor(Color.blue);
		g.drawLine(40,25,	250,180);
		g.drawLine(75,90,	400,400);
		g.setColor(Color.green);
		g.drawRect(10,10,	60,50);
		g.fillRect(100,10,	60,50);
		g.setColor(Color.red);
		g.drawRoundRect(190,10,	60,50,	15,15);
		g.fillRoundRect(70,90,	140,100,	30,40);
		g.setColor(Color.cyan);
		g.drawLine(20,150,	400,40);
		g.drawLine(5,290,	80,19);

		g.setXORMode(Color.black);
		g.drawLine(chsX-10, chsY, chsX+10, chsY);
		g.drawLine(chsX, chsY-10, chsX, chsY+10);
		g.setPaintMode();
	}
}

