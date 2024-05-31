package BankingManagementSystem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
	private Connection connection;
	private Scanner scanner;
	
	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public long openAccount(String email) throws SQLException {
		
		if(account_exist(email)==null) {
			scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();			
			long newAcc = generateAccountNo();
			
			
			String createAcc = "insert into accounts (acc_no, balance, email, full_name, security_pin) values (?,?,?,?,?)";
			
			PreparedStatement pr = connection.prepareStatement(createAcc);
			
			pr.setLong(1, newAcc);
			pr.setDouble(2, balance);
			pr.setString(3, email);
			pr.setString(4, full_name);
			pr.setString(5,security_pin);
			
			int rowsUpdated = pr.executeUpdate();
			
			if(rowsUpdated>0) {
				return newAcc;
			}
			else {
				throw new RuntimeException("Account creation failed!");
			}
		}
		else {
			throw new RuntimeException("Account already exists");
		}
		
	}
	
	
	public Long account_exist(String email) throws SQLException {
		String query = "select acc_no from accounts where email = ?";
		
		PreparedStatement pr = connection.prepareStatement(query);
		
		pr.setString(1, email);
		
		ResultSet rs = pr.executeQuery();
		
		if(rs.next()) {
			return rs.getLong("acc_no");
		}
		
		return null;
	}
	
	public long generateAccountNo() throws SQLException {
		String query =  "select acc_no from accounts order by acc_no desc limit 1";
		
		PreparedStatement accQuery = connection.prepareStatement(query);
		
		ResultSet rs = accQuery.executeQuery();
		
		if(rs.next()) {
			return rs.getLong(1)+1L;
		}
		else {
			return 10000456L;
		}
	}
	
	public long getAccountNumber(String email) {
		String query = "select acc_no from accounts where email = ?";
		
		try {
			PreparedStatement pr = connection.prepareStatement(query);
			
			pr.setString(1, email);
			
			ResultSet rs = pr.executeQuery();
			
			if(rs.next()) {
				return rs.getLong(1);
			}
			else {
				throw new RuntimeException("No account associate with this email");
			}
			
		}
		catch(Exception e) {
			throw new RuntimeException("No account associate with this email");
		}
	}
	
	
	@SuppressWarnings("null")
	public Long doesAccountNumberExist(long accNum) throws SQLException {
		String query = "select acc_no from accounts where acc_no = ?";
		
		PreparedStatement pr = connection.prepareStatement(query);
		
		pr.setLong(1, accNum);
		
		ResultSet rs = pr.executeQuery();
		
		if(rs.next()) return rs.getLong("acc_no");
		
		return null;
	}
}
