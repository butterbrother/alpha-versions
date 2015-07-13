import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class TextAreaDemo extends Applet {
	TextArea someText;

	public void init() {
		someText = (TextArea) add(new TextArea("Введи здесь текст", 10, 30, TextArea.SCROLLBARS_VERTICAL_ONLY));
		Button hodorButton = (Button) add(new Button("HODOR"));
		hodorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				someText.append(" HODOR");
			}
		});
	}
}
