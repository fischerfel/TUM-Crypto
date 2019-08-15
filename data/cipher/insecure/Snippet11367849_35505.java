
    package com.zenimax.encryption;

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
     * @author Matthew H. Wagner
     */
    public class TripleDesBouncyCastle {
        private static String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/PKCS7Padding";
        private static String ALGORITHM = "DESede";
        private static String BOUNCY_CASTLE_PROVIDER = "BC";

        private static void init()
        {
            Security.addProvider(new BouncyCastleProvider());
        }

        public static byte[] encode(byte[] input, byte[] key)
                throws IllegalBlockSizeException, BadPaddingException,
                NoSuchAlgorithmException, NoSuchProviderException,
                NoSuchPaddingException, InvalidKeyException {
            init();
            SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
            Cipher encrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION,
                    BOUNCY_CASTLE_PROVIDER);
            encrypter.init(Cipher.ENCRYPT_MODE, keySpec);
            return encrypter.doFinal(input);
        }

        public static byte[] decode(byte[] input, byte[] key)
                throws IllegalBlockSizeException, BadPaddingException,
                NoSuchAlgorithmException, NoSuchProviderException,
                NoSuchPaddingException, InvalidKeyException {
            init();
            SecretKey keySpec = new SecretKeySpec(key, ALGORITHM);
            Cipher decrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION,
                    BOUNCY_CASTLE_PROVIDER);
            decrypter.init(Cipher.DECRYPT_MODE, keySpec);
            return decrypter.doFinal(input);
        }
    }
