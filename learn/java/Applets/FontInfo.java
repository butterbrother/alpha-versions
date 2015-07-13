import java.awt.*;
import java.awt.event.*;

public class FontInfo extends Frame {
	public static void main(String args[]) { new FontInfo(); }

	public FontInfo() {
		super("Информация о шрифте");
		setSize(350, 60);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setVisible(true);
	}

	public void paint(Graphics g) {
		Font f = g.getFont();
		String fontName = f.getName();
		String fontFamily = f.getFamily();
		int fontSize = f.getSize();
		int fontStyle = f.getStyle();
		StringBuilder msg = new StringBuilder();
		msg.append("Семейство: ").append(fontName).append(", ");
		msg.append("Шрифт: ").append(fontFamily).append(", ");
		msg.append("Размер: ").append(Integer.toString(fontSize)).append(", ");
		msg.append("Стиль: ");

		if ((fontStyle & Font.PLAIN) == Font.PLAIN)
			msg.append("обычный ");
		if ((fontStyle & Font.BOLD) == Font.BOLD)
			msg.append("полужирный ");
		if ((fontStyle & Font.ITALIC) == Font.ITALIC)
			msg.append("курсив ");

		g.drawString(msg.toString(), 4, 32);
	}
}
