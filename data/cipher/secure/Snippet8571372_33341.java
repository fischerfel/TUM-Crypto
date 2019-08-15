// in Java-land
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

...

static String printHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(String.format("%02x", (b & 0xFF)));
    }
    return sb.toString();
}

public static Map<String,String> encrypt(String msg, String pwd, byte[] salt)
        throws Exception {
    Map<String,String> retval = new HashMap<String,String>();

    // prepare to use PBKDF2/HMAC+SHA1, since ruby supports this readily
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    // our key is 256 bits, and can be generated knowing the password and salt
    KeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt, 1024, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    // given key above, our cippher will be aes-256-cbc in ruby/openssl
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();

    // generate the intialization vector
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    retval.put("iv", printHex(iv));

    byte[] ciphertext = cipher.doFinal(msg.getBytes("UTF-8"));
    retval.put("encrypted", printHex(ciphertext));

    return retval;
}

public static void main(String[] args) throws Exception {
    String msg  = "To Ruby, from Java, with love...";
    String pwd  = "password";
    String salt = "8 bytes!"; // in reality, you would use SecureRandom!

    System.out.println("password (plaintext): " + pwd);
    System.out.println("salt: " + salt);

    Map<String,String> m = encrypt(msg, pwd, salt.getBytes());
    System.out.println("encrypted: " + m.get("encrypted"));
    System.out.println("iv: " + m.get("iv"));
}
