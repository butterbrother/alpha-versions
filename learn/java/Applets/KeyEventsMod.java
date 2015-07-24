import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class KeyEventsMod extends Applet {
	private String msg = "";
	private int X = 10, Y = 20;
	private boolean special = false;

	public void init() {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				switch (ke.getKeyCode()) {
					case KeyEvent.VK_BACK_SPACE :
						if (msg.length() > 0)
							msg = msg.substring(0, msg.length() - 1);
						special = true;
						repaint();
					default:
						special = false;
				}
			}

			public void keyTyped(KeyEvent ke) {
				if (!special)
					msg += ke.getKeyChar();
				repaint();
			}
		});
	}

	public void paint(Graphics g) {
		g.drawString(msg, X, Y);
	}
}
