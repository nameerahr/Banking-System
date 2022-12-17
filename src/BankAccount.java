import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BankAccount {
	
	protected String firstName; 
	protected String lastName;
	protected String accountPassword;
	protected int idNumber;
	private String phoneNumber;
	protected int chequingBalance;
	Scanner input = new Scanner(System.in);
	JSONParser parser = new JSONParser();

	// Constructor for existing account
	public BankAccount(int id, String password) {
		
		String fName = null;
		String lName = null;
		int chequingBal = 0;
		String phone = null;
		
		// Import account information from accounts file
		try {
	    	Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
	    	JSONArray allAccounts = (JSONArray) obj;
	    	
	        for (Object account : allAccounts) {
	            JSONObject currentAccount = (JSONObject) account;
	            
	            int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
			      
			      if (accountID == id) {
			    	  fName = (String) currentAccount.get("firstName");
					  lName = (String) currentAccount.get("lastName");
					  chequingBal = Integer.parseInt((String) currentAccount.get("chequingBalance"));				    
					  phone = (String) currentAccount.get("phoneNumber");
					  break;
			      }
	        }
		} catch (Exception e) {
			System.out.println(e);
		}

		firstName = fName;
		lastName = lName; 
		chequingBalance = chequingBal;
		accountPassword = password; 
		idNumber = id;
		phoneNumber = phone;		
	}
	
	// Constructor for new account
	public BankAccount(String fName, String lName, String password, String phone) {
		
		firstName = fName;
		lastName = lName;
		chequingBalance = 0;
		accountPassword = password;
		idNumber = generateID();
		phoneNumber = phone;
		
		// Add the account's information to the accounts file
		registerAccount();
		
		System.out.println("Account created for " + firstName + " " + lastName );
		System.out.println("Account ID is: " + idNumber);
		System.out.println("Please keep your account id and password recorded in a safe place.");		
	}
	
	public int getID() {
		return idNumber;
	}
	
	public int getChequingBalance() {
		return chequingBalance;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	// Create a random account id for new chequing accounts
	public int generateID() {
		
		Random rand = new Random(System.currentTimeMillis());
		
		// Generate a random 8 digit number for the account id
		return (rand.nextInt(90000000) + 10000000);
	}
	
	public void menuSelection() {
				
		while (true) {
			displayMenuOptions();
			
			int userSelection = input.nextInt();
				
			if (userSelection == 1) {
				makeDeposit();
			} else if (userSelection == 2) {
				makeWithdrawal();
			} else if (userSelection == 3) {
				editAccount();
			} else if (userSelection == 4) {
				viewAccount();		
			} else if (userSelection == 5) {
				accessCreditAccount();
			} else if (userSelection == 6) {
				newCreditAccount();
			} else if (userSelection == 7) {
				if(deleteAccount()) {
					return;
				}
			} else if (userSelection == 8) {
				System.out.println("You have successfully been logged out.");
				return;
			} else {
				System.out.println("Please select a valid option from the menu.");
			}
		}
	}
	
	public void displayMenuOptions() {
		
		System.out.println("Welcome to your chequing account. Please select from the following options: ");
		System.out.println("1: Make a Deposit");
		System.out.println("2: Make a Withdrawl");
		System.out.println("3: Edit Account Details");
		System.out.println("4: View Account Details");
		System.out.println("5: Access Credit Account");
		System.out.println("6: Create a New Credit Account");
		System.out.println("7: Delete Account");
		System.out.println("8: Exit Session (Log Out)");
	}
	
	public void makeDeposit() {
		
		System.out.println("Your current account balance is $" + chequingBalance);
		System.out.println("Please enter the amount you would like to deposit: ");
		
		int depositAmount = input.nextInt();
		
		chequingBalance += depositAmount;
		
		// Update the accounts file with the new balance.
		updateAccount("chequingBalance", Integer.toString(chequingBalance));
		
		System.out.println("Amount successfully deposited. Your new balance is $" + chequingBalance);
	}
	
	public void makeWithdrawal() {
		
		System.out.println("Account Balance: $" + chequingBalance);
		System.out.println("Please enter the amount you would like to withdrawl: ");
				
		while (true) {
			int withdrawalAmount = input.nextInt();
			
			// Make sure the withdrawal amount is less than or equal to the current balance
			if (withdrawalAmount <= chequingBalance) {
				chequingBalance -= withdrawalAmount;
				
				// Update the accounts file with the new balance
				updateAccount("chequingBalance", Integer.toString(chequingBalance));
				
				System.out.println("Amount successfully withdrawn. Your new balance is $" + chequingBalance);
				break;
			} else {
				System.out.println("You cannot withdraw more than your account balance. Enter a valid amount: ");
			}
		}
	}
	
	// Edit account details, such as phone number
	public void editAccount() {
		
		System.out.println("Edit Account");
		System.out.println("1: Edit Phone Number");
		System.out.println("0: Back to Main Page");
				
		while (true) {
			int selection = input.nextInt();
			
			if (selection == 1) {
				System.out.println("Current phone number on file is: " + phoneNumber);
				System.out.println("Please enter your new phone number: ");
				phoneNumber = input.next();
				
				// Update the accounts file with the new number.
				updateAccount("phoneNumber", phoneNumber);
				
				System.out.println("Phone number updated to: " + phoneNumber);
				break;
			} else if (selection == 0) {
				return;
			} else {
				System.out.println("Enter a valid selection: ");
			}
		}	
	}
	
	public void viewAccount() {
		
		System.out.println("Account Details: ");
		System.out.println("First Name: " + firstName);
		System.out.println("Last Name: " + lastName);
		System.out.println("Account Balance: $" + chequingBalance);
		System.out.println("Account ID: " + idNumber);
		System.out.println("Phone Number: " + phoneNumber);
		System.out.println("0: Back to Main Page");
		
		int selection = input.nextInt();
		
		if (selection == 0) {
			return;
		}
	}
	
	public void accessCreditAccount() {
		
		// Make sure there is an existing credit account associated with the current chequing account before accessing it
		if(checkCreditAccountExists()) {
			CreditAccount creditAccount = new CreditAccount(idNumber, accountPassword);
			creditAccount.creditMenuSelection();
		} else {
			System.out.println("You do not have an existing credit account. Please select '6' to create one.");
		}
	}
	
	public void newCreditAccount() {
		
		// Make sure the current chequing account does not have a credit account already associated with it 
		if(checkCreditAccountExists()) {
			System.out.println("You already have a credit account. Please select '5' to access it.");
		} else {
			System.out.println("Please enter your desired credit limit for your credit account: $500      or      $1000");
			System.out.println("0: Back to Main Page");
			
			while (true) {
				int creditLimit = input.nextInt();
				
				if (creditLimit == 500 || creditLimit == 1000) {
					CreditAccount creditAccount = new CreditAccount(idNumber, accountPassword, creditLimit);
					creditAccount.creditMenuSelection();
					break;
				} else if (creditLimit == 0) {
					return;
				} else {
					System.out.println("Enter a valid limit or navigate back: ");
				}
			}
		}		
	}
	
	// Check to see whether there is a credit account associated with the current chequing account
	public boolean checkCreditAccountExists() {
		
		boolean creditAccountExists = false;
		
		try {
		    Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
		   	JSONArray allAccounts = (JSONArray) obj;
		   	
		   	for (Object account : allAccounts) {
		   		JSONObject currentAccount = (JSONObject) account;
		            
		        int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
			      
		        if (accountID == idNumber) {
					if (currentAccount.containsKey("creditLimit")) {
						creditAccountExists = true;
					}
					
					break;
		        }
	        }
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return creditAccountExists;
	}
	
	// Remove the account from the accounts file after confirmation
	public boolean deleteAccount() {
		
		System.out.println("Enter 'YES' to confirm the deletion of your account (This will also remove any associated credit accounts).");
		System.out.println("Enter 'NO' if you would like to keep your account.");
		
		String confirmation = input.next();
			
		// If confirmed, remove the account from the accounts file
		if (confirmation.equals("YES")) {
			try {
			    Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
			   	JSONArray allAccounts = (JSONArray) obj;
			   	
			   	for (Object account : allAccounts) {
			   		JSONObject currentAccount = (JSONObject) account;
			            
			        int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
				      
			        if (accountID == idNumber) {
						allAccounts.remove(currentAccount);
						    	  
						FileWriter file = new FileWriter("/Users/nehanmohammed/Desktop/Accounts.json");
						file.write(allAccounts.toJSONString());
						file.flush();
						file.close();
						break;
			        }
		        }
			   	
				System.out.println("Account was successfully deleted.");			   	
			   	return true;
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			System.out.println("Deletion was not confirmed. Your account was not deleted.");
			return false;
		}
		
		return false;
	}
	
	// Add the account as a new entry on file 
	public void registerAccount() {

        try {
            Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
            JSONArray allAccounts = (JSONArray) obj;

            JSONObject newAccount = new JSONObject();
            newAccount.put("firstName", firstName);
            newAccount.put("lastName", lastName);
            newAccount.put("ID", Integer.toString(idNumber));
            newAccount.put("password", accountPassword);
            newAccount.put("chequingBalance", Integer.toString(chequingBalance));
            newAccount.put("phoneNumber", phoneNumber);

            allAccounts.add(newAccount);

            FileWriter file = new FileWriter("/Users/nehanmohammed/Desktop/Accounts.json");
            file.write(allAccounts.toJSONString());
            file.flush();
            file.close();

        } catch (Exception e) {
            System.out.println(e);
        }
	}
	
	// Update account information on file
	public void updateAccount(String key, String value) {
		
		try {
	    	Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
	    	JSONArray allAccounts = (JSONArray) obj;
	    	
	        for (Object account : allAccounts) {
	            JSONObject currentAccount = (JSONObject) account;
	            
	            int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
			      
			      if (accountID == idNumber) {
			    	  currentAccount.put(key, value);
			    	  
			    	  FileWriter file = new FileWriter("/Users/nehanmohammed/Desktop/Accounts.json");
			          file.write(allAccounts.toJSONString());
			          file.flush();
			          file.close();
			    	  break;
			      }
	        }
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}