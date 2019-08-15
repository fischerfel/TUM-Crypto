public class testEncrypt {
public static void main(String[] args) throws Exception {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    char[] password = "passkey".toCharArray();

    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[8];
    random.nextBytes(salt);

    KeySpec spec = new PBEKeySpec(password, salt, 1000, 256); 
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] ciphertext = cipher.doFinal("301a7fed-54e4-4ae2-9b4d-6db057f75c91".getBytes("UTF-8"));

    System.out.println(ciphertext.length);

}
