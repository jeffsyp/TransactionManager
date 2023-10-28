package banking;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Manages user transactions by processing command-line input.
 * Supports transactions such as opening accounts, closing accounts,
 * depositing money, withdrawing money, and printing account details.
 *
 * @author Altay Ozkan
 * @author Jeffery Sypytkowski
 */
public class TransactionManager {
    private Scanner scanner;
    private boolean running;
    private AccountDatabase accountDatabase;

    /**
     * Initializes the TransactionManager.
     */
    public TransactionManager() {
        scanner = new Scanner(System.in);
        running = false;
        accountDatabase = new AccountDatabase();
    }

    /**
     * Starts the TransactionManager's loop, processing commands until stopped.
     */
    public void run() {
        running = true;
        System.out.println("Transaction Manager is running.");

        while (running) {
            String commandLine = scanner.nextLine();
            processCommand(commandLine);
        }

        System.out.println("Transaction Manager is terminated.");
    }

    /**
     * Processes the provided command.
     *
     * @param commandLine String containing the user's command.
     */
    private void processCommand(String commandLine) {
        StringTokenizer tokenizer = new StringTokenizer(commandLine);
        if (tokenizer.hasMoreTokens()) {
            String command = tokenizer.nextToken();
            switch (command) {
                case "Q" -> running = false;
                case "O" -> openAccount(tokenizer);
                case "C" -> closeAccount(tokenizer);
                case "D" -> depositMoney(tokenizer);
                case "W" -> withdrawMoney(tokenizer);
                case "P" -> accountDatabase.printSorted();
                case "PI" -> accountDatabase.printFeesAndInterests();
                case "UB" -> accountDatabase.printUpdatedBalances();
                default -> System.out.println("Invalid command!");
            }
        }
    }

    /**
     * Processes the 'O' command to open a new account.
     *
     * @param tokenizer Tokenized input for easy data extraction.
     */
    private void openAccount(StringTokenizer tokenizer) {
        try {
            String type = tokenizer.nextToken();
            Profile profile = new Profile(tokenizer.nextToken(), tokenizer.nextToken(),
                    new Date(tokenizer.nextToken()));
            if (!dateCheck(profile.getDob())) {
                return;
            }
            double balance = Double.parseDouble(tokenizer.nextToken());
            if (balance <= 0) {
                System.out.println("Initial deposit cannot be 0 or negative.");
                return;
            }
            Account buffer = createTemp(type, profile);
            boolean validAcct = true;
            switch (type) {
                case "CC" -> validAcct = createCC(buffer, profile, balance, Integer.parseInt(tokenizer.nextToken()));
                case "S" ->
                    validAcct = createS(buffer, profile, balance, Integer.parseInt(tokenizer.nextToken()));
                case "MM" -> validAcct = createMM(buffer, profile, balance);
                default -> validAcct = createC(buffer, profile, balance);
            }
            if (validAcct) {
                System.out.println(profile.getFname() + " " + profile.getLname() + " " + profile.getDob() + "(" + type
                        + ") opened.");
            }
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Missing data for opening an account.");
        } catch (NumberFormatException e) {
            System.out.println("Not a valid amount.");
        }
    }

    /**
     * Creates a MoneyMarket account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     */
    private boolean createMM(Account buffer, Profile profile, double balance) {
        if (profile.getDob().getAge() < 16) {
            System.out.println("DOB invalid: " + profile.getDob() + " under 16.");
            return false;
        }
        if (balance >= 2000) {
            if (!accountDatabase.contains(buffer)) {
                accountDatabase.open(new MoneyMarket(profile, balance, 0));
                return true;
            } else {
                System.out.println(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                        + "(MM) is already in the database.");
                return false;
            }
        } else {
            System.out.println("Minimum of $2000 to open a Money Market account.");
            return false;
        }
    }

    /**
     * Creates a CollegeChecking account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @param code    Code for the campus of account being added.
     */
    private boolean createCC(Account buffer, Profile profile, double balance, int code) {
        if (profile.getDob().getAge() >= 24) {
            System.out.println("DOB invalid: " + profile.getDob() + " over 24.");
            return false;
        }
        if (code >= 0 && code <= 2) {
            if (!accountDatabase.contains(buffer)) {
                accountDatabase.open(new CollegeChecking(profile, balance, code));
                return true;
            } else {
                System.out.println(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                        + "(CC) is already in the database.");
                return false;
            }
        } else {
            System.out.println("Invalid campus code.");
            return false;
        }
    }

    /**
     * Creates a Savings account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @param code    Code for the isLoyal status of account being added.
     */
    private boolean createS(Account buffer, Profile profile, double balance, int code) {
        if (!accountDatabase.contains(buffer)) {
            accountDatabase.open(new Savings(profile, balance, code));
            return true;
        } else {
            System.out.println(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                    + "(S) is already in the database.");
            return false;
        }
    }

    /**
     * Creates a Checking account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     */
    private boolean createC(Account buffer, Profile profile, double balance) {
        if (!accountDatabase.contains(buffer)) {
            accountDatabase.open(new Checking(profile, balance));
            return true;
        } else {
            System.out.println(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                    + "(C) is already in the database.");
            return false;
        }
    }

    /**
     * Processes the 'C' command to close an existing account.
     *
     * @param tokenizer Tokenized input for easy data extraction.
     */
    private void closeAccount(StringTokenizer tokenizer) {
        try {
            // Extract necessary information to identify and close the account.
            String type = tokenizer.nextToken();
            Profile profile = new Profile(tokenizer.nextToken(), tokenizer.nextToken(),
                    new Date(tokenizer.nextToken()));
            if (!dateCheck(profile.getDob())) {
                return;
            }
            Account account = createTemp(type, profile);
            // Check if account exists and then try to close it.
            if (accountDatabase.contains(account)) {
                if (account.getClass() == accountDatabase.getAccount(account).getClass()) {
                    if (accountDatabase.close(account)) {
                        System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                                + account.getHolder().getDob() + "(" + type + ") has been closed.");
                    } else {
                        System.out.println("Error closing account.");
                    }
                } else {
                    System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                            + account.getHolder().getDob() + "(" + type + ") is not in the database.");
                }
            } else {
                System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") is not in the database.");
            }
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Missing data for closing an account.");
        }
    }

    /**
     * Creates a buffer account object.
     *
     * @param type    The type of account.
     * @param profile The profile associated with the account.
     * @return A new Account object.
     */
    private Account createTemp(String type, Profile profile) {
        switch (type) {
            case "CC":
                return new CollegeChecking(profile, 0, 0);
            case "S":
                return new Savings(profile, 0, 0);
            case "MM":
                return new MoneyMarket(profile, 0, 0);
            default:
                return new Checking(profile, 0);
        }
    }

    /**
     * Creates a temporary account object for balance update.
     *
     * @param type    the type of the account (e.g., "CC", "S", "MM").
     * @param profile the profile of the account holder.
     * @param balance the balance to be set for the account.
     * @return the newly created account object with the specified balance.
     */
    private Account createUpdateBalance(String type, Profile profile, double balance) {
        switch (type) {
            case "CC":
                return new CollegeChecking(profile, balance, 0);
            case "S":
                return new Savings(profile, balance, 0);
            case "MM":
                return new MoneyMarket(profile, balance, 0);
            default:
                return new Checking(profile, balance);
        }
    }

    /**
     * Processes the 'D' command to deposit money into an account.
     *
     * @param tokenizer Tokenized input for easy data extraction.
     */
    private void depositMoney(StringTokenizer tokenizer) {
        try {
            // Extract necessary information to identify the account and deposit amount.
            String type = tokenizer.nextToken();
            Profile profile = new Profile(tokenizer.nextToken(), tokenizer.nextToken(),
                    new Date(tokenizer.nextToken()));
            double amount = Double.parseDouble(tokenizer.nextToken());
            if (amount <= 0) {
                System.out.println("Deposit - amount cannot be 0 or negative.");
                return;
            }
            Account account = createUpdateBalance(type, profile, amount);
            // Attempt to deposit the specified amount into the account.
            accountDatabase.deposit(account);
            if (account.getBalance() == Constants.ACCOUNT_FOUND) {
                System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") Deposit - balance updated.");
            } else {
                System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") is not in the database.");
            }
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Missing data for deposit.");
        } catch (NumberFormatException e) {
            System.out.println("Not a valid amount.");
        }
    }

    /**
     * Validates the given date, ensuring it's not a future date.
     *
     * @param date the date to be validated.
     * @return true if the date is valid and not in the future, false otherwise.
     */
    private static boolean dateCheck(Date date) {
        Date todaysDate = new Date();
        if (!date.isValid()) {
            System.out.println("DOB invalid: " + date + " not a valid calendar date!");
            return false;
        }
        if ((date.getYear() > todaysDate.getYear())
                || (date.getYear() == todaysDate.getYear() && date.getMonth() > todaysDate.getMonth())
                || (date.getYear() == todaysDate.getYear() && date.getMonth() == todaysDate.getMonth()
                        && date.getDay() >= todaysDate.getDay())) {
            System.out.println("DOB invalid: " + date + " cannot be today or a future day.");
            return false;
        }
        return true;
    }

    /**
     * Processes the 'W' command to withdraw money from an account.
     *
     * @param tokenizer Tokenized input for easy data extraction.
     */
    private void withdrawMoney(StringTokenizer tokenizer) {
        try {
            // Extract necessary information to identify the account and withdrawal amount.
            String type = tokenizer.nextToken();
            Profile profile = new Profile(tokenizer.nextToken(), tokenizer.nextToken(),
                    new Date(tokenizer.nextToken()));
            double amount = Double.parseDouble(tokenizer.nextToken());
            if (amount <= 0) {
                System.out.println("Withdraw - amount cannot be 0 or negative.");
                return;
            }
            Account account = createUpdateBalance(type, profile, amount);
            // Attempt to withdraw the specified amount from the account.
            if (accountDatabase.withdraw(account)) {
                System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") Withdraw - balance updated.");
            } else if (account.getBalance() == Constants.NOT_FOUND) {
                System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") is not in the database.");
            } else {
                System.out.println(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") Withdraw - insufficient fund.");
            }
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Missing data for withdrawal.");
        } catch (NumberFormatException e) {
            System.out.println("Not a valid amount.");
        }
    }
}