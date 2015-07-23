import java.awt.*;
import java.awt.event.*;

public class GridBagDemo extends Frame {
	public static void main(String args[]) {
		new GridBagDemo();
	}

	public GridBagDemo() {
		super("Пример работы GridBagLayout");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.
