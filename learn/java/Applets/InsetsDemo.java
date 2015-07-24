import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class InsetsDemo extends Frame {
	public static void main(String args[]) { new InsetsDemo(); }

	public InsetsDemo() {
		super("Insets, для отступа от краёв");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setBackground(Color.cyan);
		setLayout(new BorderLayout());

		add(new Button("Кнопка сверху"), BorderLayout.NORTH);
		add(new Label("Сообщение нижнего колонтитула", Label.CENTER), BorderLayout.SOUTH);
		add(new Button("Слева"), BorderLayout.WEST);
		add(new Button("Справа"), BorderLayout.EAST);

		add(new TextArea("Некоторое сообщение"), BorderLayout.CENTER);

		setSize(getLayout().preferredLayoutSize(this));
		setVisible(true);
	}

	public Insets getInsets() {
		return new Insets(30, 30, 30, 30);
	}
}
