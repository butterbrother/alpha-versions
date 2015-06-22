package Utils.ResourceBundle;

import java.util.*;

public class SampleRB_ru_RU extends ListResourceBundle {
	protected Object[][] getContents() {
		Object[][] resources = new Object[3][2];

		resources[0][0] = "title";
		resources[0][1] = "Моя программа";

		resources[1][0] = "StopText";
		resources[1][1] = "Стоп";

		resources[2][0] = "StartText";
		resources[2][1] = "Старт";

		return resources;
	}
}
