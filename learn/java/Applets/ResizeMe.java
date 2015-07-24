import java.awt.*;
import java.awt.event.*;

public class ResizeMe extends Frame {
	final int inc = 25, nzY = 20;
	int max = 500;
	int min = 200;
	Dimension d;

	public ResizeMe() {
		super("Щёлкни по мне");
		setSize(200,230);
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) {
				int w = (d.width + inc) > max ? min : (d.width + inc);
				int h = (d.height + inc + nzY) > max ? min : (d.height + inc + nzY);
				setSize(new Dimension(w, h));
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		d = getSize();
		g.drawLine(0, 0+nzY, d.width-1, d.height-1);
		g.drawLine(0, d.height-1, d.width-1, 0+nzY);
		g.drawRect(0,0+nzY, d.width-1, d.height-1);
	}

	public static void main(String args[]) {
		new ResizeMe();
	}
}
