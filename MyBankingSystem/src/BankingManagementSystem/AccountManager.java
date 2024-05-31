package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	private Connection connection;
	private Scanner scanner;
	
	public AccountManager(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public double checkBalance(Long acc_no) {
		String balance_query = "select balance from accounts where acc_no = ?";
		
		
		try {
			PreparedStatement pr = connection.prepareStatement(balance_query);
			pr.setLong(1, acc_no);
			
			ResultSet rs = pr.executeQuery();
			
			if(rs.next()) {
				return rs.getDouble("balance");
			}
			throw new RuntimeException("No bank balance");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error");
		}
	}
	
	public void transferMoney(long sender_account_number) throws SQLException {
		
		scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
	        if(sender_account_number!=0 && receiver_account_number!=0){
	            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE acc_no = ? and security_pin = ?");
	            preparedStatement.setLong(1, sender_account_number);
	            preparedStatement.setString(2,security_pin);
	            
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                double current_balance = resultSet.getDouble("balance");
	                if (amount<=current_balance){

	                    // Write debit and credit queries
	                    String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?";
	                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?";

	                    // Debit and Credit prepared Statements
	                    PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
	                    PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

	                    // Set Values for debit and credit prepared statements
	                    creditPreparedStatement.setDouble(1, amount);
	                    creditPreparedStatement.setLong(2, receiver_account_number);
	                    debitPreparedStatement.setDouble(1, amount);
	                    debitPreparedStatement.setLong(2, sender_account_number);
	                    int rowsAffected1 = debitPreparedStatement.executeUpdate();
	                    int rowsAffected2 = creditPreparedStatement.executeUpdate();
	                    if ( rowsAffected2 > 0) {
	                        System.out.println("Transaction Successful!");
	                        System.out.println("Rs."+amount+" Transferred Successfully");
	                        connection.commit();
	                        connection.setAutoCommit(true);
	                        return;
	                    } else {
	                        System.out.println("Transaction Failed");
	                        connection.rollback();
	                        connection.setAutoCommit(true);
	                    }
	                }else{
	                    System.out.println("Insufficient Balance!");
	                }
	            }else{
	                System.out.println("Invalid Security Pin!");
	            }
	        }else{
	            System.out.println("Invalid account number");
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
}
