import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CreditAccount extends BankAccount{
	
	private int availableCredit;
	private int creditLimit;

	// Constructor for existing account
	public CreditAccount(int id, String password) {
		
		super(id, password);

		int availableCred = 0;
		int creditLim = 0;
			
		// Import account information from accounts file
		try {
	    	Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
		    JSONArray allAccounts = (JSONArray) obj;
		    	
		    for (Object account : allAccounts) {
		    	JSONObject currentAccount = (JSONObject) account;
		            
		         int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
				      
				 if (accountID == id) {
					 availableCred = Integer.parseInt((String) currentAccount.get("availableCredit"));	
					 creditLim = Integer.parseInt((String) currentAccount.get("creditLimit"));	
					 break;
				 }
		    }
		} catch (Exception e) {
			System.out.println(e);
		}

		availableCredit = availableCred;
		creditLimit = creditLim;
					
		System.out.println("Successfully opened " + firstName + " " + lastName + "'s credit account.");				
	}
		
	// Constructor for new account
	public CreditAccount(int id, String password, int creditLim) {
		
		super(id, password);

		availableCredit = creditLim;
		creditLimit = creditLim;
			
		// Add the account's information to the accounts file
		registerAccount();
			
		System.out.println("Credit account created for " + firstName + " " + lastName );
		System.out.println("Your credit limit is $" + creditLimit);		
	}
	
	public void creditMenuSelection() {
		
		while (true) {
			displayCreditMenuOptions();
			
			int userSelection = input.nextInt();
				
			if (userSelection == 1) {
				makePurchase();
			} else if (userSelection == 2) {
				viewCreditInformation();
			} else if (userSelection == 3) {
				transferToCredit();
			} else if (userSelection == 4) {
				payCreditBill();
			} else if (userSelection == 5) {
				if(deleteCreditAccount()) {
					return;
				}
			} else if (userSelection == 6) {
				System.out.println("Returning to chequing account.");
				return;
			} else {
				System.out.println("Please select a valid option from the menu.");
			}
		}
	}
	
	public void displayCreditMenuOptions() {
		
		System.out.println("Welcome to your credit account. Please select from the following options: ");
		System.out.println("1: Make a Purchase With Your Credit Card");
		System.out.println("2: View Credit Information");
		System.out.println("3: Make a Transfer to Your Credit Account");
		System.out.println("4: Pay Credit Bill");
		System.out.println("5: Delete Credit Account");
		System.out.println("6: Return to Chequing Account");
	}
	
	public void makePurchase() {
		
		System.out.println("Available Credit: $" + availableCredit);
		System.out.println("Please enter the amount of your purchase: ");
		
		while (true) {
			int purchaseAmount = input.nextInt();
			
			// Make sure the purchase amount is less than or equal to the available credit
			if (purchaseAmount <= availableCredit) {
				availableCredit -= purchaseAmount;
				
				// Update the accounts file with the new balance
				updateAccount("availableCredit", Integer.toString(availableCredit));
				
				System.out.println("Purchase of $" + purchaseAmount + " successfully made. New available credit amount is $" + availableCredit);
				break;
			} else {
				System.out.println("Your purchase amount is greater than your available credit. Purchase declined.");
				System.out.println("Enter a valid amount: ");
			}
		}		
	}
	
	public void viewCreditInformation() {
		
		System.out.println("Available Credit: $" + availableCredit);
		System.out.println("Credit Limit: $" + creditLimit);
		
		double billAmount = (creditLimit - availableCredit)*0.1;
		System.out.println("Minimum Credit Bill Amount: $" + billAmount);

		System.out.println("0: Back to Main Page");
		
		int selection = input.nextInt();
		
		if (selection == 0) {
			return;
		}
	}
	
	// Transfer funds from chequing to credit account
	public void transferToCredit() {
		
		System.out.println("Available Credit: $" + availableCredit);
		
		double billAmount = (creditLimit - availableCredit)*0.1;
		System.out.println("Minimum Credit Bill Amount: $" + billAmount);
		
		System.out.println("Please enter the amount you would like to transfer from your chequing to credit account: ");

		
		while (true) {
			int transferAmount = input.nextInt();
			
			// Make sure the transfer amount is less than or equal to the chequing balance and does not cause the available credit to exceed the credit limit
			if ((transferAmount <= chequingBalance) && (transferAmount <= (creditLimit - availableCredit))) {
				availableCredit += transferAmount;
				chequingBalance -= transferAmount;
				
				// Update the accounts file with the new chequing balance and available credit
				updateAccount("availableCredit", Integer.toString(availableCredit));
				updateAccount("chequingBalance", Integer.toString(chequingBalance));
				
				System.out.println("Successful transfer of $" + transferAmount + " from chequing to credit acount.");
				break;
			} else if (transferAmount > chequingBalance) {
				System.out.println("You do not have the funds for this transfer. Your transfer amount is greater than your available chequing balance. Transfer unsuccessful.");
				System.out.println("Enter a valid amount: ");
			} else if (transferAmount > (creditLimit - availableCredit)) {
				System.out.println("Transfer amount will cause your available credit to exceed your credit limit. Transfer unsuccessful.");
				System.out.println("Enter a valid amount: ");
			}
		}
	}
	
	// Credit bill is 10% of the difference between the credit limit and available credit
	public void payCreditBill() {
		
		double billAmount = (creditLimit - availableCredit)*0.1;
		System.out.println("Minimum Credit Bill Amount: $" + billAmount);
		
		System.out.println("Enter '1' to confirm that you would like to pay your credit bill.");
		System.out.println("0: Back to Main Page");

		int payBill = input.nextInt();
		
		// Make sure the transfer amount is less than or equal to the chequing balance
		if (payBill == 1) {
			if (billAmount <= chequingBalance) {
				availableCredit += billAmount;
				chequingBalance -= billAmount;
				
				// Update the accounts file with the new chequing balance and available credit
				updateAccount("availableCredit", Integer.toString(availableCredit));
				updateAccount("chequingBalance", Integer.toString(chequingBalance));
				
				System.out.println("Credit bill successfully paid.");
			} else {
				System.out.println("You do not have the funds to pay your bill. Your bill amount is greater than your available chequing balance. Bill payment unsuccessful.");
			}
		} else if (payBill == 0) {
		} else {
			System.out.println("Selection was invalid. Returning to main page.");
		}
		
	}
	
	public boolean deleteCreditAccount() {
		
		System.out.println("Enter 'YES' to confirm the deletion of your account.");
		System.out.println("Enter 'NO' if you would like to keep your account.");
		
		String confirmation = input.next();
			
		// If confirmed, remove the credit account associated with the chequing account from the accounts file
		if (confirmation.equals("YES")) {
			try {
			    Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
			   	JSONArray allAccounts = (JSONArray) obj;
			   	
			   	for (Object account : allAccounts) {
			   		JSONObject currentAccount = (JSONObject) account;
			            
			        int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
				      
			        if (accountID == idNumber) {
						currentAccount.remove("availableCredit");
						currentAccount.remove("creditLimit");
						    	  
						FileWriter file = new FileWriter("/Users/nehanmohammed/Desktop/Accounts.json");
						file.write(allAccounts.toJSONString());
						file.flush();
						file.close();
						break;
			        }
		        }
			   	
				System.out.println("Credit account was successfully deleted. Returning to chequing account.");
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
		
	// Add the credit account to the chequing account on file
	public void registerAccount() {
		
		try {
		   	Object obj = parser.parse(new FileReader("/Users/nehanmohammed/Desktop/Accounts.json"));
		   	JSONArray allAccounts = (JSONArray) obj;
		    	
		   	for (Object account : allAccounts) {
		   		JSONObject currentAccount = (JSONObject) account;
		            
		        int accountID = Integer.parseInt(((String) currentAccount.get("ID")));
				      
		        if (accountID == idNumber) {
		        	currentAccount.put("creditLimit", Integer.toString(creditLimit));
		        	currentAccount.put("availableCredit", Integer.toString(availableCredit));
				    	  
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
