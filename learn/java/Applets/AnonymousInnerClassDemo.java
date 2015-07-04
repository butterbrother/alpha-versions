import java.applet.*;
import java.awt.event.*;

public class AnonymousInnerClassDemo extends Applet {
	public void init() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				showStatus("Mouse Pressed");
			}
		});
	}
}
