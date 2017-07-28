package io.supercharge.corebanklogic;

public class BankAccount {
	private long balance=0;
	private String accountNumberString=null;
	private BankAccount() {
		//csak a megfelelõ konstruktort hívhatja
	}
	public BankAccount(long balance, String accountNumberString) {
		super();
		this.balance = balance;
		this.accountNumberString = accountNumberString;
	}

	public void addDeposit(long amount) {
		balance+=amount;
	}
	
	public void withDrawMoney(long amount) {
		balance-=amount;
	}
	
	public String getAccountNumberString() {
		return accountNumberString;
	}

	public void setAccountNumberString(String accountNumberString) {
		this.accountNumberString = accountNumberString;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}
	public long getBalance() {
		return balance;
	}

}
