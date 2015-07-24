import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ScrollbarDemo extends Applet implements AdjustmentListener {
	Scrollbar vert, horiz;

	public void init() {
		vert = (Scrollbar) add(new Scrollbar(Scrollbar.VERTICAL, 0, 1, 0, Integer.parseInt(getParameter("height"))));
		vert.setPreferredSize(new Dimension(20, 100));

		horiz = (Scrollbar) add(new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, Integer.parseInt(getParameter("width"))));
		horiz.setPreferredSize(new Dimension(100, 20));

		vert.addAdjustmentListener(this);
		horiz.addAdjustmentListener(this);

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				vert.setValue(e.getY());
				horiz.setValue(e.getX());
				repaint();
			}
		});
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		repaint();
	}

	public void paint(Graphics g) {
		String msg = "Вертикально: " + vert.getValue() + ", горизонтально: " + horiz.getValue();
		g.drawString(msg, 6, 160);

		g.drawString("*", horiz.getValue(), vert.getValue());
	}
}
