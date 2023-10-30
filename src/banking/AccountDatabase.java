package banking;

import java.text.DecimalFormat;

/**
 * This class represents a database of various banking accounts.
 * It includes operations to manipulate and retrieve account data.
 * 
 */
public class AccountDatabase {
    private Account[] accounts; // list of various types of accounts
    private int numAcct; // number of accounts in the array

    /**
     * Default constructor initializes the account database with
     * a default initial capacity.
     */
    public AccountDatabase() {
        this.accounts = new Account[Constants.INITIAL_CAPACITY];
        this.numAcct = 0;
    }

    /**
     * Finds the index of a specific account in the database.
     * 
     * @param account The account to find.
     * @return The index of the account if found, otherwise returns
     *         Constants.NOT_FOUND.
     */
    private int find(Account account) {
        for (int i = 0; i < numAcct; i++) {
            if (accounts[i].getHolder().getFname().toUpperCase().equals(account.getHolder().getFname().toUpperCase())
                    && accounts[i].getHolder().getLname().toUpperCase()
                            .equals(account.getHolder().getLname().toUpperCase())
                    && accounts[i].getHolder().getDob().equals(account.getHolder().getDob())) {
                return i;
            }
        }
        return Constants.NOT_FOUND;
    }

    /**
     * Finds the index of a specific account in the database based on
     * the account holder's details and exact account type.
     *
     * @param account The account to find based on exact details.
     * @return The index of the account if found, otherwise returns
     *         Constants.NOT_FOUND.
     */
    private int findExact(Account account) {
        for (int i = 0; i < numAcct; i++) {
            if (accounts[i].getHolder().getFname().toUpperCase().equals(account.getHolder().getFname().toUpperCase())
                    && accounts[i].getHolder().getLname().toUpperCase()
                            .equals(account.getHolder().getLname().toUpperCase())
                    && accounts[i].getHolder().getDob().equals(account.getHolder().getDob())
                    && accounts[i].getClass() == account.getClass()) {
                return i;
            }
        }
        return Constants.NOT_FOUND;
    }

    /**
     * Increases the size of the accounts array to accommodate more accounts.
     */
    private void grow() {
        Account[] newAccounts = new Account[accounts.length + Constants.GROWTH_AMOUNT];
        for (int i = 0; i < numAcct; i++) {
            newAccounts[i] = accounts[i];
        }
        accounts = newAccounts;
    }

    /**
     * Checks if the database contains a specific account.
     * 
     * @param account The account to check.
     * @return true if the account exists, false otherwise.
     */
    public boolean contains(Account account) {
        int index = find(account);
        if (index != Constants.NOT_FOUND && (accounts[index] instanceof Checking && account instanceof Checking)) {
            return true;
        } else if (index != Constants.NOT_FOUND && (accounts[index].getClass() != account.getClass())
                && findExact(account) == Constants.NOT_FOUND) {
            return false;
        }
        return index != Constants.NOT_FOUND;
    }

    /**
     * Retrieves the account details from the database based on the account
     * holder's first name, last name, and date of birth.
     *
     * @param account The account to find based on holder's details.
     * @return The account if found, otherwise returns null.
     */
    public Account getAccount(Account account) {
        for (int i = 0; i < numAcct; i++) {
            if (accounts[i].getHolder().getFname().toUpperCase().equals(account.getHolder().getFname().toUpperCase())
                    && accounts[i].getHolder().getLname().toUpperCase()
                            .equals(account.getHolder().getLname().toUpperCase())
                    && accounts[i].getHolder().getDob().equals(account.getHolder().getDob())) {
                return accounts[i];
            }
        }
        return null;
    }

    /**
     * Adds a new account to the database.
     * If the database is full, it will expand.
     * 
     * @param account The account to be added.
     * @return true if the account was added successfully, false otherwise.
     */
    public boolean open(Account account) {
        if (numAcct >= accounts.length) {
            grow();
        }
        if (numAcct < accounts.length) {
            accounts[numAcct] = account;
            numAcct++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a specific account from the database.
     * 
     * @param account The account to be removed.
     * @return true if the account was removed successfully, false otherwise.
     */
    public boolean close(Account account) {
        int index = find(account);
        if (index == Constants.NOT_FOUND)
            return false;
        for (int i = index; i < numAcct - 1; i++) {
            accounts[i] = accounts[i + 1];
        }
        numAcct--;
        accounts[numAcct] = null;
        return true;
    }

    /**
     * Processes a withdrawal operation on a specific account.
     * 
     * @param account The account from which to withdraw. The balance of this
     *                account object represents the amount to be withdrawn.
     * @return true if the withdrawal was successful, false otherwise.
     */
    public boolean withdraw(Account account) {
        int index = findExact(account);
        if (index == Constants.NOT_FOUND) {
            account.setBalance(Constants.NOT_FOUND);
            return false; // Account not found
        }
        if (accounts[index].balance < account.getBalance()) {
            return false; // Insufficient funds
        }
        accounts[index].balance -= account.getBalance(); // Deduct the amount from the balance
        if (accounts[index] instanceof MoneyMarket) {
            ((MoneyMarket) accounts[index]).incrementWithdrawals();
        }
        return true;
    } // false if insufficient fund

    /**
     * Processes a deposit operation on a specific account.
     * 
     * @param account The account in which to deposit. The balance of this
     *                account object represents the amount to be deposited.
     */
    public void deposit(Account account) {
        int index = findExact(account);
        if (index != Constants.NOT_FOUND) {
            accounts[index].balance += account.getBalance(); // Update the balance
            account.setBalance(Constants.ACCOUNT_FOUND);
        }
    }

    /**
     * Sorts the accounts in the database based on their type and profile.
     */
    private void sortAccounts() {
        for (int i = 0; i < numAcct - 1; i++) {
            for (int j = 0; j < numAcct - i - 1; j++) {
                String type1 = accounts[j].getClass().getName();
                String type2 = accounts[j + 1].getClass().getName();
                int cmpType = type1.compareTo(type2);
                if (cmpType > 0) {
                    Account temp = accounts[j];
                    accounts[j] = accounts[j + 1];
                    accounts[j + 1] = temp;
                } else if (cmpType == 0) {
                    Profile profile1 = accounts[j].getHolder();
                    Profile profile2 = accounts[j + 1].getHolder();
                    int cmpProfile = profile1.compareTo(profile2);
                    if (cmpProfile > 0) {
                        Account temp = accounts[j];
                        accounts[j] = accounts[j + 1];
                        accounts[j + 1] = temp;
                    }
                }
            }
        }
    }

    /**
     * Prints the sorted list of accounts to the console.
     */
    public void printSorted() {
        sortAccounts();
        if (numAcct == 0) {
            System.out.println("Account Database is empty!");
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            System.out.println("\n*Accounts sorted by account type and profile.");
            for (int i = 0; i < numAcct; i++) {
                Account index = accounts[i];
                if (index instanceof MoneyMarket) {
                    System.out.print("Money Market::Savings::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()));
                    if (((MoneyMarket) index).isLoyal()) {
                        System.out.print("::is loyal");
                    }
                    System.out.println("::withdrawal: 0");
                } else if (index instanceof Savings) {
                    System.out.print("Savings::" + index.getHolder().getFname() + " " + index.getHolder().getLname()
                            + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()));
                    if (((Savings) index).isLoyal()) {
                        System.out.print("::is loyal");
                    }
                    System.out.println();
                } else if (index instanceof CollegeChecking) {
                    System.out.println("College Checking::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()) + "::" + ((CollegeChecking) index).getCampus());
                } else if (index instanceof Checking) {
                    System.out.println("Checking::" + index.getHolder().getFname() + " " + index.getHolder().getLname()
                            + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()));
                }
            }
            System.out.println("*end of list.\n");
        }
    }

    /**
     * Prints detailed account information including fees and monthly interests.
     */
    public void printFeesAndInterests() {
        sortAccounts();
        if (numAcct == 0) {
            System.out.println("Account Database is empty!");
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            System.out.println("\n*list of accounts with fee and monthly interest");
            for (Account index : accounts) {
                if (index instanceof MoneyMarket) {
                    System.out.print("Money Market::Savings::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()));
                    if (((MoneyMarket) index).isLoyal())
                        System.out.print("::is loyal");
                    System.out.println("::withdrawal: " + ((MoneyMarket) index).getWithdrawals() + "::fee $"
                            + decimalFormat.format(index.monthlyFee()) + "::monthly interest $"
                            + decimalFormat.format(index.monthlyInterest()));
                } else if (index instanceof Savings) {
                    System.out.print("Savings::" + index.getHolder().getFname() + " " + index.getHolder().getLname()
                            + " " + index.getHolder().getDob() + "::Balance $" + decimalFormat.format(index.getBalance()));
                    if (((Savings) index).isLoyal())
                        System.out.print("::is loyal");
                    System.out.println("::fee $" + decimalFormat.format(index.monthlyFee()) + "::monthly interest $"
                            + decimalFormat.format(index.monthlyInterest()));
                } else if (index instanceof CollegeChecking) {
                    System.out.println("College Checking::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()) + "::" + ((CollegeChecking) index).getCampus()
                            + "::fee $" + decimalFormat.format(index.monthlyFee()) + "::monthly interest $"
                            + decimalFormat.format(index.monthlyInterest()));
                } else if (index instanceof Checking) {
                    System.out.println("Checking::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(index.getBalance()) + "::fee $"
                            + decimalFormat.format(index.monthlyFee()) + "::monthly interest $"
                            + decimalFormat.format(index.monthlyInterest()));}
            }
            System.out.println("*end of list.\n");
        }
    }

    /**
     * Prints the accounts after applying the monthly fees and interests to their balances.
     */
    public void printUpdatedBalances() {
        sortAccounts();
        if (numAcct == 0) {
            System.out.println("Account Database is empty!");
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            System.out.println("\n*list of accounts with fees and interests applied.");
            for (Account index : accounts) {
                double updatedBalance = 0;
                if (index != null)
                    updatedBalance = index.getBalance() + index.monthlyInterest() - index.monthlyFee();
                if (index instanceof MoneyMarket) {
                    ((MoneyMarket) index).resetWithdrawals();
                    System.out.print("Money Market::Savings::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(updatedBalance));
                    if (((MoneyMarket) index).isLoyal())
                        System.out.print("::is loyal");
                    System.out.println("::withdrawal: " + ((MoneyMarket) index).getWithdrawals());
                } else if (index instanceof Savings) {
                    System.out.print("Savings::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(updatedBalance));
                    if (((Savings) index).isLoyal())
                        System.out.print("::is loyal");
                    System.out.println("");
                } else if (index instanceof CollegeChecking) {
                    System.out.println("College Checking::" + index.getHolder().getFname() + " "
                            + index.getHolder().getLname() + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(updatedBalance) + "::" + ((CollegeChecking) index).getCampus());
                } else if (index instanceof Checking) {
                    System.out.println("Checking::" + index.getHolder().getFname() + " " + index.getHolder().getLname()
                            + " " + index.getHolder().getDob() + "::Balance $"
                            + decimalFormat.format(updatedBalance));
                }
            }
            System.out.println("*end of list.\n");
        }
    }
}