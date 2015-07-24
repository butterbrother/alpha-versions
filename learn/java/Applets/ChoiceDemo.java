// Choice - поле со списком
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ChoiceDemo extends Applet implements ItemListener {
	private Choice os, browser;
	private Button reset;

	public void init() {
		os = (Choice) add (new Choice());
		os.add("Windows");
		os.add("Linux");
		os.add("Macos");
		os.add("Solaris");
		os.addItemListener(this);

		browser = (Choice) add (new Choice());
		browser.add("Internet explorer");
		browser.add("Mozilla Firefox");
		browser.add("Google chrome");
		browser.addItemListener(this);

		reset = (Button) add (new Button("Сброс"));
		reset.setActionCommand("reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				os.select(0);
				browser.select(0);
				repaint();
			}
		});
	}

	public void itemStateChanged(ItemEvent e) {
		repaint();
	}

	public void paint(Graphics g) {
		g.drawString("Selected OS: " + os.getSelectedItem() + " (" + (os.getSelectedIndex()+1) + "/" + os.getItemCount() + ")", 6, 40);
		g.drawString("Selected browser: " + browser.getSelectedItem()+1 + " (" + (browser.getSelectedIndex()+1) + "/" + browser.getItemCount() + ")", 6, 60);
	}
}
