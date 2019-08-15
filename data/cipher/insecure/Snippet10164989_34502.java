

    import java.security.InvalidKeyException;
    import java.security.NoSuchAlgorithmException;
    import java.security.NoSuchProviderException;
    import java.security.Security;
    import javax.crypto.BadPaddingException;
    import javax.crypto.Cipher;
    import javax.crypto.IllegalBlockSizeException;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.SecretKeySpec;
    import org.bouncycastle.jce.provider.BouncyCastleProvider;


    /**
     *
     * @author Jose Luis Montes de Oca
     */
    public class TripleDesCipher {
       private static String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/Nopadding";
       private static String ALGORITHM = "DESede";
       private static String BOUNCY_CASTLE_PROVIDER = "BC";
       private Cipher encrypter;
       private Cipher decrypter;

       public TripleDesCipher(byte[] key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
             InvalidKeyException {
          Security.addProvider(new BouncyCastleProvider());
          SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
          encrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
          encrypter.init(Cipher.ENCRYPT_MODE, keySpec);
          decrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
          decrypter.init(Cipher.DECRYPT_MODE, keySpec);
       }

       public byte[] encode(byte[] input) throws IllegalBlockSizeException, BadPaddingException {
          return encrypter.doFinal(input);
       }

       public byte[] decode(byte[] input) throws IllegalBlockSizeException, BadPaddingException {
          return decrypter.doFinal(input);
       }
    }

