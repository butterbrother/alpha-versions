import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class TextFieldDemo extends Applet implements ActionListener {
	TextField name, pass;
	Checkbox showPass;

	public void init() {
		add(new Label("Имя: ", Label.RIGHT));
		name = (TextField) add(new TextField(12));

		add(new Label("Пароль: ", Label.RIGHT));
		pass = (TextField) add(new TextField(20));
		pass.setEchoChar('*');

		name.addActionListener(this);
		pass.addActionListener(this);

		showPass = (Checkbox) add(new Checkbox("Показывать пароль", false));
		showPass.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (showPass.getState()) {
					pass.setEchoChar((char)0);
				} else {
					pass.setEchoChar('*');
				}
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void paint(Graphics g) {
		g.drawString("Имя: " + name.getText(), 6, 60);
		g.drawString("Выделенный текст в имени: " + name.getSelectedText(), 6, 80);
		g.drawString("Пароль: " + pass.getText(), 6, 100);
	}
}
