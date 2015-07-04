import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class AppWindow extends Frame {
	String keymsg = "This is a test.";
	String mousemsg = "";
	int mouseX=30, mouseY=30;

	public AppWindow() {
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				keymsg += ke.getKeyChar();
				repaint();
			};
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				mouseX = me.getX();
				mouseY = me.getY();
				mousemsg = "Mouse Down at " + mouseX + ", " + mouseY;
				repaint();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void paint(Graphics g) {
		g.drawString(keymsg, 10, 40);
		g.drawString(mousemsg, mouseX, mouseY);
	}

	public static void main(String args[]) {
		AppWindow appwin = new AppWindow();

		appwin.setSize(new Dimension(300, 200));
		appwin.setTitle("An AWT-Based Application");
		appwin.setVisible(true);
	}
}
