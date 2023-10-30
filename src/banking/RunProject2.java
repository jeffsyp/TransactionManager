package banking;

/**
 * The entry point for running the Transaction Manager application.
 * It creates an instance of TransactionManager and runs it.
 *
 */
public class RunProject2 {

    /**
     * The main method that is executed when the program is run.
     * It creates a new TransactionManager object.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new TransactionManager().run();
    }
}