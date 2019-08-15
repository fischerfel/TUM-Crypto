public class Security {

public static void main(String[] args) throws UserNotExistingException {
    Security s=new Security();
    s.signUp("John.Smith", "John Smith", "text@lau.edu", "test");
    System.out.println(s.Authenticate("John.Smith" , "test"));
}

public boolean Authenticate(String username, String password) throws UserNotExistingException {
    String dbpass = null;
    byte[] salt = null;
    try {
        // Load driver for connecting to db
        Class.forName("com.mysql.jdbc.Driver");
        // Establishing connection to db
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vote sys", "root", "");
        // Creating statement object to be executed on dbms
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select  pass, salt from user_acc where username = '" + username + "';");

        if (rs.next()) {
            dbpass = rs.getString(2);
            String temp = rs.getString(2);
            System.out.println(temp);
            salt = temp.getBytes();
        }
        for (byte i : salt)
            System.out.print(i);
        System.out.println();
        // Terminating connection to db
        con.close();
    } catch (Exception e) {
        System.out.println(e);
    }
    if (dbpass == null || salt == null)
        throw new UserNotExistingException("User " + username + " doesn't exist");

    try { //this is where im facing the problem, the condition is always returning true when its not
        String hashed=generateHash(password, salt);
        System.out.println(hashed);
        if (hashed.compareTo(dbpass)!=0)
            return false;


    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
    }

    return true;
}

private static String generateHash(String password, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
    int iterations = 1000;
    char[] chars = password.toCharArray();

    PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = skf.generateSecret(spec).getEncoded();
    return iterations + ":" + toHex(salt) + ":" + toHex(hash);
}

private static byte[] getSalt() throws NoSuchAlgorithmException {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt;
}

private static String toHex(byte[] array) throws NoSuchAlgorithmException {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();
    if (paddingLength > 0) {
        return String.format("%0" + paddingLength + "d", 0) + hex;
    } else {
        return hex;
    }
}

public void signUp(String username, String name, String email,  String password) {
    String dbuser = "", dbemail = "";
    try {
        // Load driver for connecting to db
        Class.forName("com.mysql.jdbc.Driver");
        // Establishing connection to db
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vote sys", "root", "");
        // Creating statement object to be executed on dbms
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select  username, email from user_acc where username = '" + username
                + "' or email = '" + email + "';");

        if (rs.next()) {
            dbuser = rs.getString(2);
            dbemail = rs.getString(2);
        }
        if (!dbuser.equals("") || !dbemail.equals(""))
            throw new UserNotExistingException("Username or email already exists");

        byte[] salt = getSalt();
        for (int i = 0; i < salt.length; i++) {
            System.out.print(salt[i]);
        }
        System.out.println();
        String temp= new String(salt);
        System.out.println(temp);
        String hashedPass = generateHash(password, salt);
        System.out.println(hashedPass);
        stmt.executeUpdate("INSERT INTO `user_acc`(`username`, `name`, `email`, `pass`, `salt`) VALUES ('"
                + username + "','" + name + "','" + email + "','" + hashedPass + "','" + temp + "');");

    } catch (Exception e) {
        System.out.println(e);
    }
}
