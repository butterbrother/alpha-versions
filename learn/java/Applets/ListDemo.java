import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ListDemo extends Applet implements ItemListener {
	private List os, browser;
	private Button reset;

	public void init() {
		os = (List) add(new List(4, true));
		os.add("Linux");
		os.add("Windows");
		os.add("Solaris");
		os.add("Mac OS");
		os.addItemListener(this);

		browser = (List) add(new List(3, false));
		browser.add("Mozilla Firefox");
		browser.add("Google Chrome");
		browser.add("Internet Explorer");
		browser.addItemListener(this);
		browser.select(0);

		reset = (Button) add(new Button("Сброс"));
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.select(0);
				for (int index : os.getSelectedIndexes())
					os.select(index);
				repaint();
			}
		});
	}

	public void itemStateChanged(ItemEvent e) {
		repaint();
	}

	public void paint(Graphics g) {
		StringBuilder selectedOs = new StringBuilder();
		selectedOs.append("OS selected: ");
		for (int index : os.getSelectedIndexes())
			selectedOs.append(os.getItem(index)).append(" ");
		selectedOs.append(Integer.toString(os.getSelectedIndexes().length));
		g.drawString(selectedOs.toString(), 4, 120);

		g.drawString("Browser selected: " + browser.getSelectedItem(), 4, 140);
	}
}
