import java.awt.*;
import java.awt.event.*;

public class GridLayoutDemo extends Frame {
	private int n = 4;
	public static void main(String args[]) {
		new GridLayoutDemo();
	}

	public GridLayoutDemo() {
		super("Компоновка GridLayout, сеткой");
		setLayout(new GridLayout(n, n));
		setFont(new Font("SansSerif", Font.BOLD, 24));

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int k = i * n * j;
				if (k > 0)
					add(new Button("" + k));
			}
		}

		setSize(getLayout().preferredLayoutSize(this));
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
