package io.supercharge.corebanklogic;

import io.supercharge.corebanklogic.TransactionHistory.DIRECTION;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.naming.spi.DirectoryManager;

public class CoreLogic {
	public enum FilterType{
		DATE,WITHDRAW,DEPOSIT,DEFAULT
	}
	private List<User> users = new ArrayList<User>();
	private List<TransactionHistory> history = new ArrayList<TransactionHistory>();
	private boolean transfer(BankAccount from,BankAccount to,long amount) {
		long initialBalanceFrom=from.getBalance();
		if ((initialBalanceFrom -amount > 0)) {
			from.withDrawMoney(amount);
			history.add(new TransactionHistory(from.getBalance(), (-1) * amount, new Date(),DIRECTION.WITHDRAW));
			if (from.getBalance()-amount==initialBalanceFrom) {
				long initialBalanceTo=to.getBalance();
				to.addDeposit(amount);
				if ((initialBalanceTo+amount)==to.getBalance()) {
					history.add(new TransactionHistory(to.getBalance(), amount, new Date(),DIRECTION.DEPOSIT));
					return true;
				}else {
					to.setBalance(initialBalanceTo);
					return false;
				}
			} else {
				//rollback
				from.setBalance(initialBalanceFrom);
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean withDraw(BankAccount acc,long amount) {
		if (acc.getBalance() - amount >0) {
			acc.withDrawMoney(amount);
			TransactionHistory.DIRECTION dir =DIRECTION.WITHDRAW;
			TransactionHistory h = new TransactionHistory(acc.getBalance(),Long.valueOf(amount),new Date(),dir);
			addTransactionHistory(h);
			return true;
		}  else {
			return false;	
		}

	}
	private boolean addDeposit(BankAccount acc,long amount) {
		acc.addDeposit(amount);
		TransactionHistory.DIRECTION dir =DIRECTION.DEPOSIT;
		TransactionHistory h = new TransactionHistory(acc.getBalance(),Long.valueOf(amount),new Date(),dir);
		addTransactionHistory(h);
		return true;
	}
	
	private void printTransactionHistory(FilterType filter) {
		if (filter.equals(FilterType.DEFAULT)) {
			for (TransactionHistory th : history) {
					System.out.println(th.toString());
			}
		}
		else if (filter.equals(FilterType.WITHDRAW)) {
			for (TransactionHistory th : history) {
				if (th.getDir().equals(DIRECTION.WITHDRAW))
					System.out.println(th.toString());
			}
		}else if (filter.equals(FilterType.DEPOSIT)) {
			for (TransactionHistory th : history) {
				if (th.getDir().equals(DIRECTION.DEPOSIT))
					System.out.println(th.toString());
			}
		}
	}
	
	private void printTransactionHistory(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStr=null;
        try {
			dateStr = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Rossz dátum formátum. Pl: 2010-12-01");
		}
		for (TransactionHistory th : history) {
			if (formatter.format(th.getDate()).equals(date))
				System.out.println(th.toString());
		}
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	private void addTransactionHistory(TransactionHistory th) {
		this.history.add(th);
	}

	public static void main(String args[]) {
		CoreLogic c=new CoreLogic();
		BankAccount ba = new BankAccount(0, "12345678-12345678-12345678");
		BankAccount ba2 = new BankAccount(0, "02345678-12345678-12345678");
		List<User> users = new ArrayList<User>();
		users.add(new User(ba, "Kiss", "József"));
		users.add(new User(ba, "Nagy", "Zoltán"));
		
		Scanner in=new Scanner(System.in);
		while(in.hasNextLine()) {
			final String command=in.nextLine();
			if (command.equals("q")) {
				System.out.println("Finished");
				System.exit(0);
			} else if (command.equals("addDeposit")) {
				System.out.println("Adja meg a bankszámlaszámot");
				String acc=in.nextLine();
				System.out.println("Adja meg az összeget");
				String amount=in.nextLine().replace("[a-zA-Z]", "");
				for (User user : users) {
					if (user.getAccount().getAccountNumberString().equals(acc)) {
						c.addDeposit(user.getAccount(), Long.valueOf(amount));
					}
				}
			} else if (command.equals("withDraw")) {
				System.out.println("Adja meg a bankszámlaszámot");
				String acc=in.nextLine();
				System.out.println("Adja meg az összeget");
				String amount=in.nextLine().replace("[a-zA-Z]", "");
				for (User user : users) {
					if (user.getAccount().getAccountNumberString().equals(acc)) {
						if (c.withDraw(user.getAccount(), Long.valueOf(amount))) {
							System.out.println("Sikeres tranzakció");
						} else {
							System.out.println("Sikertelen tranzakció");
						}
						
					}
				}
			}
			else if (command.equals("history")) {
				System.out.println("Filter? I vagy N");
				String filter=in.nextLine();
				if (filter.equalsIgnoreCase("I")) {
					System.out.println("Filter típus? DATE,WITHDRAW,DEPOSIT");
					String filterType=in.nextLine();
					if (filterType.equalsIgnoreCase("WITHDRAW")) {
						c.printTransactionHistory(FilterType.WITHDRAW);
					} else if (filterType.equalsIgnoreCase("DEPOSIT")) {
						c.printTransactionHistory(FilterType.DEPOSIT);
					} else if (filterType.equalsIgnoreCase("DATE")) {
						String date=in.nextLine();
						c.printTransactionHistory(date);
					}
					
				} else {
					c.printTransactionHistory(FilterType.DEFAULT);
				}
			} else if (command.equals("transfer")) {
				System.out.println("Adja meg forrás a bankszámlaszámot");
				String acc1=in.nextLine();
				System.out.println("Adja meg a cél bankszámlaszámot");
				String acc2=in.nextLine();
				System.out.println("Adja meg az összeget");
				String amount=in.nextLine().replace("[a-zA-Z]", "");
				User source=null;
				User target=null;
				for (User user : users) {
					if (user.getAccount().getAccountNumberString().equals(acc1)) {
						source=user;
					}
					
					if (user.getAccount().getAccountNumberString().equals(acc2)) {
						target=user;
					}
				}
				if (source!=null && target!=null) {
					if (c.transfer(source.getAccount(), target.getAccount(), Long.valueOf(amount))) {
						System.out.println("Sikeres tranzakció!");
					} else {
						System.out.println("Sikertelen tranzakció!");
					}
					
				} else {
					System.out.println("Sikertelen tranzakció!");
				}
			}
			
		}
	}
}
