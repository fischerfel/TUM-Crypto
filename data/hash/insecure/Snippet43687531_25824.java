Exception in thread "main" java.util.MissingFormatArgumentException: Format specifier '%s'
    at java.util.Formatter.format(Unknown Source)
    at java.io.PrintStream.format(Unknown Source)
    at java.io.PrintStream.printf(Unknown Source)
    at User.printAccountSummary(User.java:120)
    at ATM.printUserMenu(ATM.java:66)
    at ATM.main(ATM.java:25)

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    /*
     * The first name of the user
     */
    private String firstName;

    /**
     * The last name of the user
     */
    private String lastName;

    /*
     * The id number of the user
     */
    private String uuid;

    /**
     * The MDS hash of the user's pin number. 
     */
    private byte pinHash[];

    /**
     * The list of accounts for this user
     */
    private ArrayList<Account> accounts; 

    /**
     * 
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param pin the user's account pin number 
     * @param theBank the bank object that the user is customer of
     */

    public User(String firstName, String lastName, String pin, Bank theBank) 
    {
        //Set user's name
        this.firstName = firstName;
        this.lastName = lastName; 

        //store the pin's MDS hash, rather than the original value, for serurity reasons
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        //get a new, unique universal ID for the user
        this.uuid = theBank.getNewUserUUID(); 

        //create empty list of accounts
        this.accounts = new ArrayList<Account>(); 

        //print log message
        System.out.printf("New user %s, %s with IS %s created.\n", lastName, firstName, this.uuid);


    }

    /**
     * Add on account for user 
     * @param onAcct the account to add
     */
    public void addAccount(Account onAcct)
    {
        this.accounts.add(onAcct);
    }

    /**
     * Return the user's UUID
     * @return the uuid 
     */
    public String getUUID()
    {
        return this.uuid; 
    }

    /**
     * @param aPin the pin to check 
     * @return whether the pin is valid or not 
     */

    public boolean validatePin(String aPin)
    {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        return false; 
    }

    /**
     * 
     * @return first name
     */
    public String getFirstName()
    {
        return this.firstName; 
    }

    /**
     * Print summaries for the accounts of this user.
     */

    public void printAccountSummary(){
        System.out.printf("\n\n%s's accounts summary\n" + this.firstName);
        for(int i = 0; i < this.accounts.size(); i++){
            System.out.printf("(%d) %s\n", i+1, this.accounts.get(i).getSummaryLine()); 

        }
        System.out.println();
    }

    /**
     * Get the number of accounts of the user 
     * @return the number of accounts
     */
    public int numAccounts(){
        return this.accounts.size(); 
    }

    /**
     * Print transaction history for a certain acocunt. 
     * @param acctIdx
     */
    public void printAcctTransHistory(int acctIdx){
        this.accounts.get(acctIdx).printTransHistory(); 
    }

    /**
     * Get the balance of a particular account 
     * @param acctIdx the index of the account to use 
     * @return the balance of account 
     */
    public double getAcctBalance(int acctIdx){
        return this.accounts.get(acctIdx).getBalance();
    }

    /**
     * Get the UUID of a particular account 
     * @param acctIdx: the index of the account to use 
     * @return the UUID of the account 
     */
    public String getAcctUUID(int acctIdx){
        return this.accounts.get(acctIdx).getUUID();
    }

    /**
     * Add a transaction to a particular account 
     * @param acctIdx the index of the account 
     * @param amount the amount of the transaction 
     * @param memo the memo of the transaction 
     */

    public void addAcctTransaction(int acctIdx, double amount, String memo){
        this.accounts.get(acctIdx).addTransaction(amount, memo); 
    }
}
