import java.awt.*;
import java.awt.event.*;

public class GridBagDemo extends Frame {
	private GridBagConstraints gbc, special;
	private TextField Tgridx, Tgridy;

	public static void main(String args[]) {
		new GridBagDemo();
	}

	public GridBagDemo() {
		super("Пример работы GridBagLayout");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();

		special = new GridBagConstraints();
		// Расположение в ячейке
		special.gridx = 0;
		special.gridy = 3;


		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new Label("gridx:", Label.RIGHT), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.5;
		Tgridx = new TextField(Integer.toString(special.gridx));
		add(Tgridx, gbc);

		gbc.gridx = 4;
		gbc.weightx = 0.0;
		add(new Label("gridy:", Label.RIGHT), gbc);

		gbc.gridx = 5;

		setVisible(true);
		setSize(getLayout().preferredLayoutSize(this));
	}
}
