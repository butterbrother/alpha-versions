import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class CheckboxGroupDemo extends Applet implements ItemListener {
	Checkbox win, linux, solaris, mac;
	CheckboxGroup chbg;

	public void init() {
		chbg = new CheckboxGroup();
		win = new Checkbox("Windows", chbg, true);
		linux = new Checkbox("Linux", chbg, false);
		solaris = new Checkbox("Solaris", chbg, false);
		mac = new Checkbox("Mac", chbg, false);

		add(win);
		add(linux);
		add(solaris);
		add(mac);

		win.addItemListener(this);
		linux.addItemListener(this);
		solaris.addItemListener(this);
		mac.addItemListener(this);
	}

	public void itemStateChanged(ItemEvent e) {
		repaint();
	}

	public void paint(Graphics g) {
		g.drawString("Выбрано: " + chbg.getSelectedCheckbox().getLabel(), 6, 80);
	}
}
