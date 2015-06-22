package Utils;

import java.util.Calendar;

class CalendarDemo {
	public static void main(String args[]) {
		String months[] = {
			"Jan", "Feb", "Mar", "Apr",
			"May", "Jun", "Jul", "Aug",
			"Sep", "Oct", "Nov", "Dec"
		};

		Calendar calendar = Calendar.getInstance();

		System.out.println(
				"Дата: "
				+ months[calendar.get(Calendar.MONTH)]
				+ " " + calendar.get(Calendar.DATE) + " "
				+ calendar.get(Calendar.YEAR)
				);
		System.out.println(
				"Время: "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND)
				);

		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 29);
		calendar.set(Calendar.SECOND, 22);

		System.out.println(
				"Изменённое время: "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND)
				);
	}
}
