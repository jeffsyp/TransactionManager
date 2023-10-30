# TransactionManager

This is a simple software to process the banking transactions for RU Bank. The transactions will be
entered through the command lines on the terminal. The software is an interactive system to produce the output
whenever a transaction is entered. The software shall be able to process transactions of opening an account, closing
an existing account, depositing money to an existing account, withdrawing money from an existing account, and print
the details of all accounts. RU Bank provides four types of banking accounts listed in the table below. Note that, same
person can hold different types of accounts, however, cannot hold a College Checking and Checking at the same time.
For all account types, must be age of 16 or older to open, for College Checking, must be under the age of 24 to open.
The interest rates and monthly fees are different based on the account types and account options.

A banking transaction is a command line that always begins with a command, in uppercase letters, and followed by
several data tokens delimited by one or more spaces. Commands are case-sensitive, which means the commands
with lowercase letters are invalid. The software supports the following commands.

• O command, to open an account with the desired account type. There are 4 account types: C – checking, CC –
college checking, S – savings, MM – money market savings. Each account holder is identified by his/her profile,
which includes first name, last name, and date of birth. There must be an initial deposit to open the account. When
an account is opened, the account is added to the account database. Below is a list of sample transactions for
opening an account. Note that, it is possible that the user didn’t enter enough data tokens for opening an account.
O C John Doe 2/19/2000 599.99
O CC Jane Doe 10/1/2000 999.99 0
O S april march 1/15/1987 1500 1
O MM Roy Brooks 10/31/1979 2909.10
The above transactions start with the O command followed by the account type, first name, last name, date of
birth, and the amount of the initial deposit. When opening a College Checking, a campus code is required: 0 –
New Brunswick, 1 – Newark, 2 – Camden. Other campus codes are invalid. Savings accounts must enter the loyal
customer status: 0 – non-loyal customer, 1 – loyal customer. Money Market requires a minimum of $2000 to open,
and it is set to loyal customer status by default. The names are not case-sensitive. If the date of birth entered is
today or a future date, it is invalid.

• C command, to close an existing account. When an account is closed, it will be removed from the account
database. Below are the sample transactions.
C MM Jane Doe 10/1/1995
C C April March 1/15/1987
C CC John Doe 2/19/1989
C S April March 1/15/1987

• D command, to deposit money to an existing account. A transaction will be rejected if an invalid amount is
entered. Below are the sample transactions.
D C John Doe 2/19/1990 100
D CC Kate Lindsey 8/31/2001 100
D MM Roy Brooks 10/31/1979 100.99
D S April March 1/15/1987 100

• W command, to withdraw money from an existing account. Command line formats are the same with the D
command, and the same rules apply. For Money Market accounts, the loyal customer status will be unchecked if balance dropped below $2000.

• P command to display all the accounts in the account database, sorted by the account types.

• PI command, to display all the accounts in the account database, the same order with the P command. In addition,
displays the calculated fees and monthly interests based on current account balances.

• UB command, to update the account balance for all accounts by applying the fees and interests earned. This
resets the number of withdrawals of the Money Market accounts to 0.

• Q command, to stop the program execution and display "Transaction Manager is terminated."
