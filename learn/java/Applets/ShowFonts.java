import java.awt.*;
import java.awt.event.*;

public class ShowFonts extends Frame {
	public static void main(String args[]) {
		new ShowFonts();
	}

	public ShowFonts() {
		super("Отображение шрифтов");
		setSize(550,60);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		StringBuilder msg = new StringBuilder();
		String FontList[];

		// Получить шрифты можно от графического окружения
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		FontList = ge.getAvailableFontFamilyNames();

		for (String item: FontList)
			msg.append(item).append(", ");
		g.drawString(msg.toString(), 4, 32);
	}
}
