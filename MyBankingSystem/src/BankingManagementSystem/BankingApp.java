package BankingManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class BankingApp {
	
	private static final String url = "jdbc:mysql://localhost:3306/banking_system?lockWaitTimeout=30000";
	
	private static final String username = "root";
	
	private static final String password = "Deva@7798";

	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("driver registration failed!");
		}
		
		try {
			
			Connection con = DriverManager.getConnection(url, username, password);
			Scanner sc = new Scanner(System.in);
			User user = new User(con,sc);
			Accounts acc = new Accounts(con,sc);
			AccountManager accMgr = new AccountManager(con,sc);
			
			 while(true) {
				 System.out.println("Welcome to banking app!!!");
				 System.out.println("Choose your option: ");
				 System.out.println("1 Register");
				 System.out.println("2 Sign In");
				 System.out.println("3 Exit");

				 int choice = sc.nextInt();
				 
				 switch(choice) {
				 case 1:
					 user.register();
					 break;
				 case 2:
					 String email = user.signIn();
					 
					 if(email!=null) {
							System.out.println("Successfull Logged In");
							
							Long accountNumber = acc.account_exist(email);
							
							if(accountNumber==null) {
								System.out.println("1 Open Bank Account");
								System.out.println("2 Exit");
								
								if(sc.nextInt()==1) {
									acc.openAccount(email);
								}
								else {
									break;
								}
								
							}
							else {
								System.out.println("Enter your choice");
								System.out.println("1 Check balance");
								System.out.println("2 Transfer money");
								System.out.println("3 Deposit");
								
								
								int choice2 = 0;
								
								while(choice2!=5) {
									choice2 = sc.nextInt();
									switch(choice2) {
									case 1:
										double bal = accMgr.checkBalance(accountNumber);
										System.out.println("blance: "+ bal);
										break;
									case 2:
										accMgr.transferMoney(accountNumber);
										break;
									}
								}
							}
									
						}
						else {
							System.out.println("User is not registered");
						}
					 break;
				 case 3:
					 break;
				 };
			 }
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connection failed!");
		}

	}

}
