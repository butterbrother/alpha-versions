/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murach.beans;

import java.io.Serializable;

/**
 *
 * @author somebody
 */
public class User implements Serializable {
	
	private String email;
	private String firstName;
	private String lastName;

	public User() {
		this.lastName = "";
		this.firstName = "";
		this.email = "";
	}

	public User(String email, String firstName, String lastName) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
}
