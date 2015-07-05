import java.awt.*;
import java.awt.event.*;

public class SampleFonts extends Frame {
	public static void main(String args[]) {
		new SampleFonts();
	}

	int next = -1;
	Font f;
	String msg;

	public SampleFonts() {
		super("Базовые шрифты. Щёлкайте для перечисления");
		setSize(500,100);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f = new Font("Dialog", Font.PLAIN, 12);
		msg = "Dialog";
		setFont(f);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				next++;
				switch (next) {
					case 0:
						msg = "Dialog";
						break;
					case 1:
						msg = "DialogInput";
						break;
					case 2:
						msg = "SansSerif";
						break;
					case 3:
						msg = "Serif";
						break;
					case 4:
						next = -1;
						msg = "Monospaced";
						break;
				}
				setFont(new Font(msg, Font.PLAIN, 12));
				repaint();
			}
		});

		setVisible(true);
	}

	public void paint(Graphics g) {
		g.drawString(msg, 4, 40);
	}
}
