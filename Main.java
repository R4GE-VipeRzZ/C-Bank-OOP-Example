package bankTest;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args){
		ArrayList<String> dbResult = new ArrayList<String>();
		
		dbResult = JDBC.readDB("SHOW DATABASES LIKE \"bank_db\";");	//Checks if the bank_db database exists
		
		if (dbResult.size() < 1 || "error".equals(dbResult.get(0))) {	//Creates the bank_db database if it doesn't exist
			//System.out.println("bank_db doesn't exist");
			JDBC.writeDB("create database bank_db;");
		}
		
		JDBC.setDB("bank_db");	//Changes the url used the JDBC class to the url of the bank_db database
		dbResult.clear();
		
		
		dbResult = JDBC.readDB("SHOW TABLES LIKE \"users\";");	//Checks if the users table exists
		
		if (dbResult.size() < 1 || "error".equals(dbResult.get(0))) {	//Creates the users table if it doesn't exist
			//System.out.println("users table doesn't exist");
			JDBC.writeDB("CREATE TABLE users("
					+ "userID INT AUTO_INCREMENT NOT NULL,"
					+ "firstName VARCHAR(20) NOT NULL,"
					+ "lastName VARCHAR(40) NOT NULL,"
					+ "dob DATE NOT NULL,"
					+ "phoneNum VARCHAR(11),"
					+ "eMail VARCHAR(50),"
					+ "PRIMARY KEY (userID));");
		}
		
		dbResult.clear();
		
		
		dbResult = JDBC.readDB("SHOW TABLES LIKE \"accounts\";");	//Checks if the accounts table exists
		
		if (dbResult.size() < 1 || "error".equals(dbResult.get(0))) {	//Creates the accounts table if it doesn't exist
			//System.out.println("accounts table doesn't exist");
			JDBC.writeDB("CREATE TABLE accounts("
					+ "userID INT NOT NULL,"
					+ "accountType CHAR(8) NOT NULL,"
					+ "balance INT NOT NULL,"
					+ "openedDate DATE NOT NULL,"
					+ "PRIMARY KEY (userID, accountType),"
					+ "FOREIGN KEY (userID) REFERENCES users(userID));");
		}
		
		dbResult.clear();
		
		
		dbResult = JDBC.readDB("SHOW TABLES LIKE \"address\";");	//Checks if the address table exists
		
		if (dbResult.size() < 1 || "error".equals(dbResult.get(0))) {	//Creates the address table if it doesn't exist
			//System.out.println("address table doesn't exist");
			JDBC.writeDB("CREATE TABLE address("
					+ "userID INT NOT NULL,"
					+ "addressFirstLine VARCHAR(20) NOT NULL,"
					+ "addressSecondLine VARCHAR(30) NOT NULL,"
					+ "town VARCHAR(20) NOT NULL,"
					+ "region VARCHAR(19) NOT NULL,"
					+ "country VARCHAR(16) NOT NULL,"
					+ "postcode VARCHAR(8) NOT NULL,"
					+ "PRIMARY KEY (userID),"
					+ "FOREIGN KEY (userID) REFERENCES users(userID));");
		}
		
		dbResult.clear();
		
		getMenuInput();
	}
	
	private static void getMenuInput() {	//This is the main input where a user enter what they want to do
		Scanner menuInput = new Scanner(System.in);
		Boolean count = false;
		String input = "";
		
		while (count == false){
			System.out.println("What do you want to do?\n- 1 View Balance\n- 2 Withdraw money\n- 3 Deposite money\n- 4 Add a new user/account");
			input = menuInput.next();
			
			if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")) {
				count = true;
			}else {
				System.out.println("Invalid option please try again \n");
			}
		}

		basicDetails(input);
	}
	
	private static void basicDetails(String optionSelected) {
		Boolean valuesValid = false;
		
		while (valuesValid == false){
			Scanner basicUserInfo = new Scanner(System.in);
			String firstName = "";
			String lastName = "";
			String dob = "";
			String accountType = "";
			
			System.out.println("Please enter the following\n First name:");
			firstName = '"' + basicUserInfo.nextLine() + '"';
			System.out.println("Last name:");
			lastName = '"' + basicUserInfo.nextLine() + '"';
			System.out.println("Date of birth format (YYYY-MM-DD):");
			dob = '"' + basicUserInfo.nextLine() + '"';
			System.out.println("Account type:");
			accountType = '"' + basicUserInfo.nextLine() + '"';
			
			if (optionSelected.equals("4")) {
				System.out.println("Phone number:");
				String phoneNum = '"' + basicUserInfo.nextLine() + '"';
				System.out.println("E-mail:");
				String email = '"' + basicUserInfo.nextLine() + '"';
				System.out.println("Address First Line:");
				String addressLn1 = '"' + basicUserInfo.nextLine() + '"';
				System.out.println("Address Second Line:");
				String addressLn2 = '"' + basicUserInfo.nextLine() + '"';
				System.out.println("Town/City:");
				String town = '"' + basicUserInfo.nextLine() + '"';
				System.out.println("Region:");
				String region = '"' + basicUserInfo.nextLine() + '"';
				System.out.println("Country:");
				String country= '"' + basicUserInfo.nextLine() + '"';
				System.out.println("Postcode:");
				String postcode = '"' + basicUserInfo.nextLine() + '"';
				
				AddNewUser addNewUserInput = new AddNewUser(firstName, lastName, dob, accountType, phoneNum, email, addressLn1, addressLn2, town, region, country, postcode);
				addNewUserInput.addUserToDB();
				valuesValid = true;
				getMenuInput();
			}else {
				UserDetails userDetailsInput = new UserDetails(firstName, lastName, dob, accountType);
				Boolean detailsCorrect = userDetailsInput.checkDetails();

				if (detailsCorrect == true) {
					valuesValid = true;
	
					if (optionSelected.equals("1")) {
						userDetailsInput.showBalance();
						getMenuInput();
					}else if (optionSelected.equals("2")) {
						System.out.println("Please enter the amount you would like to withdraw:");
						String withdrawNum = basicUserInfo.nextLine();
						userDetailsInput.withdrawAmount(withdrawNum);
						getMenuInput();
					}else if (optionSelected.equals("3")) {
						System.out.println("Please enter the amount you would like to debit:");
						String debitNum = basicUserInfo.nextLine();
						userDetailsInput.debitAmount(debitNum);
						getMenuInput();
					}
				}else {
					System.out.println("The no user has been found with those details please try again:\n");
				}
			}
			
			
			//basicUserInfo.close(); 		//Closing scanner will produce an error as it will also close System.in and prevent any further input
		}
	}

}
