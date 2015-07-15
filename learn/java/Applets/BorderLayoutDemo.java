import java.awt.*;
import java.awt.event.*;

public class BorderLayoutDemo extends Frame {
	public static void main(String args[]) { new BorderLayoutDemo(); }

	public BorderLayoutDemo() {
		super("Компоновщик BorderLayout");
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setLayout(new BorderLayout());

		add(new Button("Кнопка вверху."), BorderLayout.NORTH);
		add(new Label("Сюда можно поместить сообщение нижнего колонтитула."), BorderLayout.SOUTH);
		add(new Button("Справа"), BorderLayout.EAST);
		add(new Button("Слева"), BorderLayout.WEST);

		String msg = "Текстовое поле в центре";
		add(new TextArea(msg), BorderLayout.CENTER);
		
		setSize(getLayout().minimumLayoutSize(this));
	}
}
