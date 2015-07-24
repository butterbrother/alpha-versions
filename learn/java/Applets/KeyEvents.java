import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class KeyEvents extends Applet implements KeyListener {
	String msg = "";
	int X = 10, Y = 20;
	boolean special = false;

	public void init() {
		addKeyListener(this);
	}

	public void keyPressed(KeyEvent ke) {
		showStatus("Key Down");
		switch (ke.getKeyCode()) {
			case KeyEvent.VK_BACK_SPACE:
				if (msg.length() > 0)
					msg = msg.substring(0, msg.length() - 1);
				special = true;
				break;
			case KeyEvent.VK_UP:
				Y -= 10;
				special = true;
				break;
			case KeyEvent.VK_DOWN:
				Y += 10;
				special = true;
				break;
			case KeyEvent.VK_LEFT:
				X -= 10;
				special = true;
				break;
			case KeyEvent.VK_RIGHT:
				X += 10;
				special = true;
				break;
			default:
				special = false;
		}
		if (special)
			repaint();
	}

	public void keyReleased(KeyEvent ke) {
		showStatus("Key Up");
	}

	public void keyTyped(KeyEvent ke) {
		if (!special)
			msg += ke.getKeyChar();
		repaint();
	}

	public void paint(Graphics g) {
		g.drawString(msg, X, Y);
	}
}
