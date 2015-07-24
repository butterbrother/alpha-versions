import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class ButtonList extends Applet implements ActionListener {
	Button bList[] = new Button[3];
	Label msg;

	public void init() {
		Button yes = new Button("Yes");
		Button no = new Button("No");
		Button maybe = new Button("Undecided");
		msg = new Label();

		yes.setLabel("Дыа");
		no.setActionCommand("yes_cmd");

		bList[0] = (Button) add(yes);
		bList[1] = (Button) add(no);
		bList[2] = (Button) add(maybe);
		add(msg);

		for (int i = 0; i< 3; i++) {
			bList[i].addActionListener(this);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		for (int i = 0; i < 3; i++) {
			if (ae.getSource() == bList[i]) {
				msg.setText("Нажата кнопка " + bList[i].getLabel() + ", её команда - " + ae.getActionCommand());
			}
		}
	}
}
