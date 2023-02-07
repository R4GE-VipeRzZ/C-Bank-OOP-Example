package bankTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AddNewUser extends UserDetails{
	
	private String phoneNum;
	private String email;
	private String addressLn1;
	private String addressLn2;
	private String town;
	private String region;
	private String country;
	private String postcode;
	
	public AddNewUser(String inputFirstName, String inputLastName, String inputDob, String inputAccountType, String inputPhoneNum, String inputEmail, 
			String inputAddressLn1, String inputAddressLn2, String inputTown, String inputRegion, String inputCountry, String inputPostcode) {
		super(inputFirstName, inputLastName, inputDob, inputAccountType);
		this.phoneNum = inputPhoneNum;
		this.email = inputEmail;
		this.addressLn1 = inputAddressLn1;
		this.addressLn2 = inputAddressLn2;
		this.town = inputTown;
		this.region = inputRegion;
		this.country = inputCountry;
		this.postcode = inputPostcode;
	}
	
	public void addUserToDB() {
		//Adds the new user to the users table
		JDBC.writeDB("INSERT INTO users (firstName, lastName, dob, phoneNum, eMail)"
				+ "VALUES(" + this.firstName + ", " + this.lastName + ", " + this.dob + ", " + this.phoneNum + ", " + this.email + ");");
		
		//Gets the userID that was automatically assigned in the users table
		super.getUserId();
		
		//Adds the new user to the address table
		JDBC.writeDB("INSERT INTO address (userID, addressFirstLine, addressSecondLine, town, region, country, postcode)"
				+ "VALUES(" + this.userId + ", " + this.addressLn1 + ", " + this.addressLn2 + ", " + this.town 
				+ ", " + this.region + ", " + this.country + ", " + this.postcode + ");");
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = formatter.format(date);
		
		//Adds the new user to the accounts table
		JDBC.writeDB("INSERT INTO accounts (userID, accountType, balance, openedDate)"
				+ "VALUES (" + this.userId + ", " + this.accountType + ", \"0\", \"" + dateStr + "\");");
	}
}
