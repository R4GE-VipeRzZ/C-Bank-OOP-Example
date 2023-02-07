package bankTest;
import java.sql.SQLException;
import java.util.ArrayList;

class UserDetails {

	protected String userId;
	protected String firstName;
	protected String lastName;
	protected String dob;
	protected String accountType;
	
	public UserDetails(String inputFirstName, String inputLastName, String inputDob, String inputAccountType) {
		this.firstName = inputFirstName;
		this.lastName = inputLastName;
		this.dob = inputDob;
		this.accountType =inputAccountType;
	}
	
	private String calcInterest(String bal) {	//This method calcules the amount total balance with intrest taken into account depending on the account type
		Integer tempBalance = null;
		
		if (this.accountType.equals("\"Standard\"")) {	//This if is used to add the interest onto the balance
			tempBalance = Integer.parseInt(bal);
			tempBalance = tempBalance + (5 * (int)(Math.floor(tempBalance / 1000)));	//This adds 5 onto the total for every 1000 in balance
			return tempBalance.toString();
		}else {
			return bal;
		}
	}
	
	private String getBalance() {	//This method gets the current balance of the account without any interest added on
		ArrayList<String> balanceResult = new ArrayList<String>();
		
		balanceResult = JDBC.readDB("SELECT balance FROM accounts WHERE userID = " + this.userId 
									+ " AND accountType = " + this.accountType + ";");
		
		return balanceResult.get(0).toString();
	}
	
	private void setBalance(Integer newBal) {	//This method is used to update the balance of an account when withdrawing or depositing
		JDBC.writeDB("UPDATE accounts SET balance = \"" + newBal.toString() 
					+ "\" WHERE userID = " + this.userId + " AND accountType = " + this.accountType + ";");
	}
	
	public void showBalance() {	//This method with show the current balance of the users account
		String balValue = getBalance();	//Gets the current balance of the users account
		
		balValue = calcInterest(balValue);	//Called so that any interest is applied
		
		System.out.println("Your Balance is £" + balValue + "\n");
	}
	
	public void withdrawAmount(String withdrawValue) {	//This method removes the value that is passed to it form the total balance if the balance is withint the given parameters
		String balValue = getBalance();	//Gets the current balance of the users account
		Integer newBalValue = Integer.parseInt(balValue) - Integer.parseInt(withdrawValue);
		
		if (this.accountType.equals("\"Standard\"")) {
			if (newBalValue >= 0) {
				setBalance(newBalValue);
				System.out.println("£" + withdrawValue + " has been withdrawn from your account");
			}else {
				System.out.println("Sorry you do not have sufficent funds for a withdrawl of that amount");
			}
		}else {
			if (newBalValue >= -5000){
				setBalance(newBalValue);
				System.out.println("£" + withdrawValue + " has been withdrawn from your account");
			}else {
				System.out.println("Sorry you do not have sufficent funds for a withdrawl of that amount");
			}
		}
	}
	
	public void debitAmount(String debitValue) {	//This method add the value that is passed to it onto the users total balance
		String balValue = getBalance();	//Gets the current balance of the users account
		Integer newBalValue = Integer.parseInt(balValue) + Integer.parseInt(debitValue);
		setBalance(newBalValue);
		System.out.println("£" + debitValue + " has been debited to your account");
	}
	
	public void getUserId(){	//This method is used to read the userID of the user from the database and assign it to the userId attribute
		ArrayList<String> resultQuery = new ArrayList<String>();
		
		resultQuery = JDBC.readDB("SELECT * FROM users WHERE firstName = " + this.firstName 	//Check for a if the first name, last name and dob that was entered 
				+ " AND lastName = " + this.lastName 											//corresponds to a user in the users table
				+ " AND dob = " + this.dob + ";");
		
		if (resultQuery.size() > 0) {
			if (!"error".equals(resultQuery.get(0))) {
				this.userId = '"' + resultQuery.get(0) + '"';
			}else {
				this.userId = null;
			}
		}else {
			this.userId = null;
		}
	}
	
	public Boolean checkDetails() {	//This method checks that the provided details correspond to an account in the database
		Boolean detailsCorrect = false;
		ArrayList<String> queryResults = new ArrayList<String>();
		
		getUserId();
		 
		if (this.userId != null ) {		//Used to check the account type if all the previous information was correct
			detailsCorrect = true;
			ArrayList<String> accountsResults = new ArrayList<String>();
			
			//this.userId = queryResults.get(0);
			
			accountsResults = JDBC.readDB("SELECT * FROM accounts WHERE userID = "
					+ this.userId + " AND accountType = " + this.accountType + ";");

			if (accountsResults.size() < 1){	//Checks if the accountsResults array has any values and if it doesn't then make the detailsCorrect variable false
				detailsCorrect = false;			//This runs if an account type that was specified doesn't correpsond to a user with the given userID in the accounts table
			}
		}
		
		return detailsCorrect;
	}
}