import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class CheckboxDemo extends Applet implements ItemListener {
	String msg = "";
	Checkbox winXP, win7, solaris, mac;

	public void init() {
		winXP = new Checkbox("Windows XP", null, true);
		win7 = new Checkbox("Windows 7");
		solaris = new Checkbox("Solaris");
		mac = new Checkbox("Mac OS");
		add(winXP);
		add(win7);
		add(solaris);
		add(mac);

		winXP.addItemListener(this);
		win7.addItemListener(this);
		solaris.addItemListener(this);
		mac.addItemListener(this);
	}

	public void itemStateChanged(ItemEvent ie) {
		repaint();
	}

	public void paint(Graphics g) {
		msg = "Текущее состояние:";
		g.drawString(msg, 6, 80);
		msg = "Windows XP: " + winXP.getState();
		g.drawString(msg, 6, 100);
		msg = "Windows 7: " + win7.getState();
		g.drawString(msg, 6, 120);
		msg = "Solaris: " + solaris.getState();
		g.drawString(msg, 6, 140);
		msg = "Mac OS: " + mac.getState();
		g.drawString(msg, 6, 160);
	}
}
