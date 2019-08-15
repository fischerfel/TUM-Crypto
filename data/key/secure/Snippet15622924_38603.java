public class AESCrypt {
private final Cipher cipher;
private final SecretKeySpec key;
private String encryptedText, decryptedText;

public AESCrypt(String password) throws Exception {
    // hash password with SHA-256 and crop the output to 128-bit for key
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(password.getBytes("UTF-8"));
    byte[] keyBytes = new byte[16];
    System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    key = new SecretKeySpec(keyBytes, "AES");
}

public String encrypt(String plainText) throws Exception {
    byte[] iv = new byte[cipher.getBlockSize()];
    new SecureRandom().nextBytes(iv);
    AlgorithmParameterSpec spec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes());
    encryptedText = asHex(encrypted);
    return encryptedText;
}

public String decrypt(String cryptedText) throws Exception {
    byte[] iv = new byte[cipher.getBlockSize()];
    AlgorithmParameterSpec spec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    // decrypt the message
    byte[] bytes = cryptedText.getBytes("UTF-8");
    byte[] decrypted = cipher.doFinal(bytes);
    decryptedText = asHex(decrypted);
    System.out.println("Desifrovani tekst: " + decryptedText + "\n");

    return decryptedText;
}

public static String asHex(byte buf[]) {
    StringBuilder strbuf = new StringBuilder(buf.length * 2);
    int i;
    for (i = 0; i < buf.length; i++) {
        if (((int) buf[i] & 0xff) < 0x10) {
            strbuf.append("0");
        }
        strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
    }
    return strbuf.toString();
}

public static void main(String[] args) throws Exception {

    System.out.print("....AES....\n");

    String message = "MESSAGE";
    String password = "PASSWORD";

    System.out.println("MSG:" + message);

    AESCrypt aes = new AESCrypt(password);
    String encryptedText = aes.encrypt(message).toString();
    System.out.println("SIFROVANA PORUKA: " + encryptedText);
    String decryptedText = aes.decrypt(encryptedText).toString();       
    System.out.print("DESIFROVANA PORUKA: " + decryptedText);
}
