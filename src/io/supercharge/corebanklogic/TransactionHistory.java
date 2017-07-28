package io.supercharge.corebanklogic;

import java.util.Date;

public class TransactionHistory {
	public enum DIRECTION{
		WITHDRAW,DEPOSIT
	}
	private long balance=0;
	private long amount=0;
	private Date date = null;
	private DIRECTION dir;
	
	private void TransactionHistory() {
		//felülcsapjuk
	}
	public TransactionHistory(long balance, long amount, Date date,DIRECTION dir) {
		super();
		this.balance = balance;
		this.amount = amount;
		this.date = date;
		this.dir = dir;
	}
	public long getBalance() {
		return balance;
	}
	public long getAmount() {
		return amount;
	}
	public Date getDate() {
		return date;
	}
	public DIRECTION getDir() {
		return dir;
	}
	public void setDir(DIRECTION dir) {
		this.dir = dir;
	}
	@Override
	public String toString() {
		return "TransactionHistory [balance=" + balance + ", amount=" + amount
				+ ", date=" + date + ", dir=" + dir + "]";
	}
}
