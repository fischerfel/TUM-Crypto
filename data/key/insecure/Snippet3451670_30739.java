
public static void main(String[] args) throws Exception {
    String username = "bob@google.org";
    String password = "Password1";
    String secretID = "BlahBlahBlah";
    String SALT2 = "deliciously salty";

    // Get the Key
    byte[] key = (SALT2 + username + password).getBytes();
    System.out.println((SALT2 + username + password).getBytes().length);

    // Need to pad key for AES
    // TODO: Best way?

    // Generate the secret key specs.
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    // Instantiate the cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

    byte[] encrypted = cipher.doFinal((secrectID).getBytes());
    System.out.println("encrypted string: " + asHex(encrypted));

    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] original = cipher.doFinal(encrypted);
    String originalString = new String(original);
    System.out.println("Original string: " + originalString + "\nOriginal string (Hex): " + asHex(original));
}
