import java.io.FileReader;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Bank {
	
	public static void main (String args[]) {
		
		Scanner input = new Scanner(System.in);
		
		while (true) {
			mainMenuOptions();
			
			int userSelection = input.nextInt();
			
			if (userSelection == 1) {
				
				System.out.println("Please enter your account id: ");
				int id = input.nextInt();
				System.out.println("Please enter your account password: ");
				String password = input.next();
				
				while(true) {
					if (verifyAccount(id, password)) {
						BankAccount bankAccount = new BankAccount(id, password);
						System.out.println("Successfully logged in to " + bankAccount.getFirstName() + " " + bankAccount.getLastName() + "'s account #" + bankAccount.getID());		
						bankAccount.menuSelection();
						break;
					} else {
						System.out.println("Please enter your account id: ");
						id = input.nextInt();
						System.out.println("Please enter your account password: ");
						password = input.next();
					}
				}			
			} else if (userSelection == 2) {
				
				System.out.println("Please enter your first name: ");
				String firstName = input.next();
				System.out.println("Please enter your last name: ");
				String lastName = input.next();
				System.out.println("Please enter your phone number: ");
				String phoneNumber = input.next();
				System.out.println("Please enter your password (must contain at least 8 characters and must include a number): ");
				String password = input.next();
				System.out.println("Please enter your password a second time: ");
				String password2 = input.next();
				
				
				while(true) {
					if (verifyPasswordEligibility(password, password2)) {
						BankAccount bankAccount = new BankAccount(firstName, lastName, password, phoneNumber);
						bankAccount.menuSelection();
						break;
					} else {
						System.out.println("Please enter a valid password (must contain at least 8 characters and must include a number): ");
						password = input.next();
						System.out.println("Please enter your password a second time: ");
						password2 = input.next();
						
					}
				}
			} else if (userSelection == 3) {
				System.out.println("Thank you for banking with us.");
				return;
			} else {
				System.out.println("Please select a valid option from the menu.");
			}
		}		
	}
	
	public static void mainMenuOptions() {
		
		System.out.println("Welcome to the banking system. Please select from the following options: ");
		System.out.println("1: Login to Your Account");
		System.out.println("2: Create New Chequing Account");
		System.out.println("3: Exit Session");
	}
	
	// Verify that the entered password fulfills the password requirements (length and number)
	public static boolean verifyPasswordEligibility(String password, String password2) {
		
		boolean validPassword = false;
		
		if(!password.equals(password2)) {
			System.out.println("Password was not entered the same in both entries. Please try again.");
			return false;
		}
		
		if(password.length() < 8) {
			System.out.println("Password is too short.");
			return false;
		}
				
		for(char character : password.toCharArray()) {
			if(Character.isDigit(character)) {
				validPassword = true;
				break;
			}
		}
		
		if (!validPassword) {
			System.out.println("Password requires at least one number.");
		}
		
		return validPassword;
	}
	
	// Verify that the entered account exists and the password for the account is correct
	public static boolean verifyAccount(int id, String password) {

	    try {
	    	JSONParser parser = new JSONParser();
	    	Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
	    	JSONArray allAccounts = (JSONArray) obj;
	    	
		    boolean idFound = false;
	        
	        for (Object account : allAccounts) {
	            JSONObject currentAccount = (JSONObject) account;
	            
	            int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
			      
			      if (accountID == id) {
			    	  idFound = true;
			    	  
			    	  String accountPassword = (String) currentAccount.get("password");
					    
					  if (accountPassword.equals(password)) {
						  return true;
					  } else {
						  System.out.println("Password is incorrect. Please try again.");
						  return false;
					  }					    
			      }
	        }
	        
	        if (!idFound) {
	        	System.out.println("An account with this id does not exist. Please try again.");
		    	return false;
	        }
	    } catch (Exception e) {
			System.out.println(e);
		}
	
		return false;
	}
}