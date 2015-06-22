package Utils;

import java.util.Calendar;
import java.util.*;

class GregorianCalendarDemo {
	public static void main(String args[]) {
		String months[] = {
			"Янв", "Фев", "Мар", "Апр",
			"Май", "Июн", "Июл", "Авг",
			"Сен", "Окт", "Ноя", "Дек"
		};

		int year;

		GregorianCalendar gcalendar = new GregorianCalendar();

		System.out.println(
				"Дата: "
				+ months[gcalendar.get(Calendar.MONTH)]
				+ " " + gcalendar.get(Calendar.DATE) + " "
				+ (year = gcalendar.get(Calendar.YEAR))
				);

		System.out.println(
				"Время: "
				+ gcalendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ gcalendar.get(Calendar.MINUTE) + ":"
				+ gcalendar.get(Calendar.SECOND)
				);

		if (gcalendar.isLeapYear(year)) {
			System.out.println("Текущий год високосный");
		} else {
			System.out.println("Текущий год не високосный");
		}
	}
}
