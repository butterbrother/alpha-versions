/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murach.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;
import murach.beans.*;

/**
 *
 * @author somebody
 */
public class UserIO {

	private static final ReentrantLock rwLock = new ReentrantLock();

	public static User getUser(String emailAddress, String emailListPath)
		throws IOException {

		rwLock.lock();

		try (BufferedReader reader = Files.newBufferedReader(Paths.get(emailListPath))) {

			String buffer;
			User user;

			while ((buffer = reader.readLine()) != null) {
				user = readEmailRow(buffer);

				if (user.getEmail().equalsIgnoreCase(emailAddress))
					return user;
			}
		} finally {
			rwLock.unlock();
		}

		return null;
	}

	private static User readEmailRow(String inputRow) {
		String splitted[] = inputRow.split(";");

		String email = "";
		String firstName = "";
		String lastName = "";

		try {
			email = splitted[0];
			firstName = splitted[1];
			lastName = splitted[2];
		} catch (ArrayIndexOutOfBoundsException ignore) {
		}

		return new User(email, firstName, lastName);
	}
}
