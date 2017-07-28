package io.supercharge.corebanklogic;

public class User {
	private BankAccount account=null;
	private String firstName=null;
	private String lastName=null;
	private void User() {
		//defaultot felülcsapjuk
	}
	
	public User(BankAccount account, String firstName, String lastName) {
		super();
		this.account = account;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public BankAccount getAccount() {
		return account;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

}
