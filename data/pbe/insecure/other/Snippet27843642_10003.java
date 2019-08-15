public class T1 {

public static void main(String[] args) {
    String userDefinedPassword = "hello123";
    String hashedPassToStoreInDB = String.valueOf(hashPassword(userDefinedPassword));
    System.out.println("what stores in DB: " + hashedPassToStoreInDB);
    // store in database

    //Password Verify
    String inputPassword = "hello123";
    String hashedInputPassword = String.valueOf(hashPassword(inputPassword));
    System.out.println("Users hashed password: " + hashedInputPassword);

    if (hashedPassToStoreInDB.equals(hashedInputPassword)) {
        System.out.println("Correct");
    } else {
        System.out.println("Incorrect");
    }
}

private static byte[] hashPassword(String password) {
    byte[] salt = new byte[16];
    byte[] hash = null;
    for (int i = 0; i < 16; i++) {
        salt[i] = (byte) i;
    }
    try {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        hash = f.generateSecret(spec).getEncoded();

    } catch (NoSuchAlgorithmException nsale) {
        nsale.printStackTrace();

    } catch (InvalidKeySpecException ikse) {
        ikse.printStackTrace();
    }
    return hash;
}
}
