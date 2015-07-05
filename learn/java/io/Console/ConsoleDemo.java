package io.Console;

import java.io.*;

class ConsoleDemo {
	public static void main(String args[]) {
		String str;
		Console con;

		con = System.console();

		if (con == null) return;

		str = con.readLine("Введите строку: ");
		con.printf("Вот ваша строка: %s\n", str);

		char[] pass = con.readPassword("Введите пароль: ");
		System.out.println(new String(pass));
	}
}
