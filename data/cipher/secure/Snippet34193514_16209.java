import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESEncrypter {

    private static final byte[] SALT = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    public Cipher ecipher;
    public Cipher dcipher;

    AESEncrypter(String passPhrase) throws Exception {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);

        // I Think the problem is here???

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, secret);

        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = ecipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    }

    public String encrypt(String encrypt) throws Exception {
        byte[] bytes = encrypt.getBytes("UTF8");
        byte[] encrypted = encrypt(bytes);
        return new BASE64Encoder().encode(encrypted);
    }

    public byte[] encrypt(byte[] plain) throws Exception {
        return ecipher.doFinal(plain);
    }

    public String decrypt(String encrypt) throws Exception {
        byte[] bytes = new BASE64Decoder().decodeBuffer(encrypt);
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, "UTF8");
    }

    public byte[] decrypt(byte[] encrypt) throws Exception {
        return dcipher.doFinal(encrypt);
    }

   public static void main(String[] args) throws Exception {

        String message = "MESSAGE";
        String password = "PASSWORD";

        AESEncrypter encrypter1 = new AESEncrypter(password);
        AESEncrypter encrypter2 = new AESEncrypter(password);

        String encrypted1 = encrypter1.encrypt(message);
        String encrypted2 = encrypter2.encrypt(message);

        System.out.println("Display Encrypted from object 1 and 2..why do they differ?" );

        System.out.println(encrypted1) ;
        System.out.println(encrypted2) ;

        System.out.println("Display Each object decrypting its own encrypted msg. Works as expected" );

        System.out.println(encrypter1.decrypt(encrypted1)) ;
        System.out.println(encrypter2.decrypt(encrypted2)) ;

        System.out.println("Attempt to decrypt the each others msg.. will fail" );

        System.out.println(encrypter1.decrypt(encrypted2)) ;
        System.out.println(encrypter2.decrypt(encrypted1)) ;

    }

}
