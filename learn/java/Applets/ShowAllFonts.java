import java.awt.*;
import java.awt.event.*;

public class ShowAllFonts extends Frame {
	private String FontList[];
	private int position = 0;

	public static void main(String args[]) {
		new ShowAllFonts();
	}

	public ShowAllFonts() {
		super("Отображение шрифтов");
		setSize(300, 100);
		FontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				repaint();
				if (position < FontList.length)
					position++;
				else
					position = 0;
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
		setFont(new Font(FontList[position], Font.PLAIN, 14));
		g.drawString(FontList[position] + ", " + position + "/" + FontList.length, 4, 35);
	}
}
