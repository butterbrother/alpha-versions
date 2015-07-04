import java.awt.*;
import java.applet.*;

public class ParamDemo extends Applet {
	String fontName;
	int fontSize;
	float leading;
	boolean active;
	String text[];

	public void start() {
		String param;
		fontName = getParameter("fontName");
		if (fontName == null)
			fontName = "Not Found";

		param = getParameter("fontSize");
		try {
			if (param != null)
				fontSize = Integer.parseInt(param);
			else
				fontSize = 0;
		} catch (NumberFormatException e) {
			fontSize = -1;
		}

		param = getParameter("leading");
		try {
			if (param != null)
				leading = Float.valueOf(param).floatValue();
			else
				leading = 0;
		} catch (NumberFormatException e) {
			leading = -1;
		}

		param = getParameter("accountEnabled");
		if (param != null)
			active = Boolean.valueOf(param).booleanValue();

		text = new String[]{
			"Font name: " + fontName,
			"Font size: " + Integer.toString(fontSize),
			"Leading: " + Float.toString(leading),
			"Account active: " + Boolean.toString(active)
		};
			
	}

	public void paint(Graphics g) {
		int y = 10;
		if (text != null)
		for (String textLine : text) {
			g.drawString(textLine, 0, y);
			y+=16;
		}
	}
}
