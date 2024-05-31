package BankingManagementSystem;

import java.sql.Connection;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private Connection connection;
	private Scanner scanner;
	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void register() {
		scanner.nextLine();
		System.out.println("Enter your Full Name: ");
		String fullname = scanner.nextLine();
		System.out.println("Enter your Email: ");
		String email = scanner.nextLine();
		System.out.println("Enter Password:");
		String password = scanner.nextLine();
		
		if(!userExist(email)) {
			String insertQuery = "insert into user (full_name,email,password) values (?,?,?)";
			
			try {
				PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
				insertStmt.setString(1, fullname);
				insertStmt.setString(2, email);
				insertStmt.setString(3, password);
				
				int rowsInserted = insertStmt.executeUpdate();
				
				if(rowsInserted>0) {
					System.out.println("Welcome "+fullname);
				}
				else {
					System.out.println("Uh oh! Couldn't able to register try after sometime");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		else {
			System.out.println("User already existed");
		}
	}
	
	public String signIn() throws SQLException {
		scanner.nextLine();
		System.out.println("Enter your email: ");
		String email = scanner.nextLine();
		System.out.println("Enter your password: ");
		String password = scanner.nextLine();
		
		
		String findUserQuery = "select email from user where email = ? and password = ?";
		
		PreparedStatement search = connection.prepareStatement(findUserQuery);
		
		search.setString(1, email);
		search.setString(2, password);
		
		ResultSet rs = search.executeQuery();
		
		if(rs.next()) {
			return rs.getString("email");
		}
	    
		return null;
		
	}
	
	public boolean userExist(String email) {
		String userQuery = "select * from user where email = ?";
		
		try {
			PreparedStatement pr = connection.prepareStatement(userQuery);
			pr.setString(1, email);
			ResultSet rs = pr.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
