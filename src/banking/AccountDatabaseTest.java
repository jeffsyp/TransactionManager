package banking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the AccountDatabase class.
 * Specifically, it focuses on the operations to open and close accounts.
 *
 * @author Jeffery Sypytkowski
 */

public class AccountDatabaseTest {
    AccountDatabase db;
    Profile mockProfile;
    Account mockAccount;

    /**
     * Sets up the test environment before each test method is run.
     * This method initializes a mock profile, account, and an empty account
     * database.
     */
    @BeforeEach
    public void setUp() {
        db = new AccountDatabase();

        Date dateOfBirth = new Date("01/01/2000");

        mockProfile = new Profile("John", "Doe", dateOfBirth);

        mockAccount = new Account(mockProfile, 1000.00) {
            @Override
            public double monthlyInterest() {
                return 0; // mock implementation
            }

            @Override
            public double monthlyFee() {
                return 0; // mock implementation
            }
        };
    }

    /**
     * Tests the close method of the AccountDatabase class.
     * This includes adding an account to the database, closing it,
     * and ensuring that it cannot be closed again.
     */
    @Test
    public void testCloseMethod() {
        // Add an account to the database
        Assertions.assertTrue(db.open(mockAccount), "Expected account to be added successfully.");

        // Test close true case
        Assertions.assertTrue(db.close(mockAccount), "Expected account to be closed successfully.");

        // Test close false case (already closed)
        Assertions.assertFalse(db.close(mockAccount), "Expected account to not be found.");
    }
}