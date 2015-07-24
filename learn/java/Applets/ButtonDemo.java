import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ButtonDemo extends Applet implements ActionListener {
	String msg = "";
	Button yes, no, maybe;

	public void init() {
		yes = new Button("Yes");
		no = new Button("No");
		maybe = new Button("Undecided");

		add(yes);
		add(no);
		add(maybe);

		yes.addActionListener(this);
		no.addActionListener(this);
		maybe.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		String str = ae.getActionCommand();
		if (str.equals("Yes")) {
			msg = "Нажата кнопка Yes.";
		} else if (str.equals("No")) {
			msg = "Нажата кнопка No.";
		} else if (str.equals("Undecided")) {
			msg = "Нажата кнопка Undecided.";
		}
		repaint();
	}

	public void paint(Graphics g) {
		g.drawString(msg, 6, 100);
	}
}
