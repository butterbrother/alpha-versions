import java.awt.*;
import java.applet.*;

public class LabelDemo extends Applet {
	public void init() {
		Label one = new Label("Первая");
		add(one);
		Label two = new Label("Вторая");
		add(two);
		Label three = new Label("Никакая");
		add(three);
		three.setText("Третья");
		two.setAlignment(Label.CENTER);
	}
}
