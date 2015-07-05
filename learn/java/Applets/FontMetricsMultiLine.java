import java.awt.*;
import java.awt.event.*;

public class FontMetricsMultiLine extends Frame {
	private int curY=20, curX=0;
	public static void main(String args[]) { new FontMetricsMultiLine(); }

	public FontMetricsMultiLine() {
		super("Отображение текста с переносом");
		setSize(300, 100);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setFont(new Font("SansSerif", Font.PLAIN, 12));
		setVisible(true);
	}

	private void nextLine(String s, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		curY += fm.getHeight();
		curX = 0;
		g.drawString(s, curX, curY);
		curX = fm.stringWidth(s);
	}

	private void sameLine(String s, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		g.drawString(s, curX, curY);
		curX += fm.stringWidth(s);
	}

	public void paint(Graphics g) {
		nextLine("Это в строке 1", g);
		nextLine("Это в строке 2", g);
		sameLine(" Это в той же строке", g);
		sameLine(" И это тоже", g);
		nextLine("Это в строке 3", g);
		curX = 0;
		curY = 20;
	}
}
