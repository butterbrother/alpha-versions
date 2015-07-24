import java.awt.*;
import java.awt.event.*;

public class CardLayout2 extends Frame {
	Checkbox unix, win;
	CheckboxGroup osGroup;

	Panel winGroup;
	Checkbox winXP, win7, win8;

	Panel unixGroup;
	Checkbox linux, solaris, bsd;

	CardLayout OScardLO;
	Panel OSCard;

	public static void main(String args[]) { new CardLayout2(); }

	public CardLayout2() {
		super("Очередной пример работы CardLayout");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		OSCard = new Panel();
		add(OSCard);

		OScardLO = new CardLayout();
		OSCard.setLayout(OScardLO);
		
		winGroup = new Panel();
		OSCard.add(winGroup, "WinG");
		winXP = (Checkbox) winGroup.add(new Checkbox("Windows XP"));
		win7 = (Checkbox) winGroup.add(new Checkbox("Windows 7"));
		win8 = (Checkbox) winGroup.add(new Checkbox("Windows 8"));

		unixGroup = new Panel();
		OSCard.add(unixGroup, "UnixG");
		linux = (Checkbox) unixGroup.add(new Checkbox("Linux"));
		solaris = (Checkbox) unixGroup.add(new Checkbox("Solaris"));
		bsd = (Checkbox) unixGroup.add(new Checkbox("BSD"));

		osGroup = new CheckboxGroup();
		unix = (Checkbox) add(new Checkbox("*nix group", true, osGroup));
		win = (Checkbox) add(new Checkbox("win group", false, osGroup));
		unix.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeGroup(e);
			}
		});
		win.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeGroup(e);
			}
		});

		setVisible(true);
		setSize(getLayout().preferredLayoutSize(this));
	}

	public void changeGroup(ItemEvent e) {
	}
}
