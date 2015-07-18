import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class CardLayoutDemo extends Applet {
	Checkbox winXP, win7, solaris, mac;
	Panel osCards;
	CardLayout cardLO;
	Button Win, Other;

	public void init() {
		Win = new Button("Windows");
		Other = new Button("Other");
		add(Win);
		add(Other);

		cardLO = new CardLayout();
		osCards = new Panel();
		osCards.setLayout(cardLO);

		winXP = new Checkbox("Windows XP", true);
		win7 = new Checkbox("Windows 7");
		solaris = new Checkbox("Solaris");
		mac = new Checkbox("Mac OS");

		Panel winPan = new Panel();
		winPan.add(winXP);
		winPan.add(win7);

		Panel otherPan = new Panel();
		otherPan.add(solaris);
		otherPan.add(mac);

		osCards.add(winPan, "Windows");
		osCards.add(otherPan, "Other");

		add(osCards);

		Win.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAction(e);
			}
		});

		Other.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAction(e);
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				cardLO.next(osCards);
			}
		});
	}

	public void doAction(ActionEvent e) {
		if (e.getSource() == Win) {
			cardLO.show(osCards, "Windows");
		} else {
			cardLO.show(osCards, "Other");
		}
	}
}
