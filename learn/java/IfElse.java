class IfElse {
	public static void main(String args[]) {
		int month = 9;
		String months[] = { "нет такого", "январь", "февраль",
			"март", "апрель", "май", "июнь", "июль", "август",
			"сентябрь", "окрябрь", "ноябрь", "декабрь" };
		String season;

		if (month == 12 || month == 1 || month == 2)
			season = "зиме";
		else if (month == 3 || month == 4 || month == 5)
			season = "весне";
		else if (month == 6 || month == 7 || month == 8)
			season = "лету";
		else if (month == 9 || month == 10 || month == 11)
			season = "осени";
		else
			season = "несуществующему месяцу";

		System.out.println("Месяц " + months[((month >=1 && month <= 12) ? month : 0)] + " относится к " + season);
	}
}
