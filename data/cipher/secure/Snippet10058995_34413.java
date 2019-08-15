import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final int    KEY_LENGTH              = 128;
    private static final int    ITERATIONS              = 100;

    private static final String ALGORITHM               = "AES";
    private static final String SECRET_KEY_ALGORITHM    = "PBKDF2WithHmacSHA1";
    private static final String TRANSFORMATION          = "AES/CBC/PKCS5Padding";

    private final Cipher        m_enc_cipher;
    private final Cipher        m_dec_cipher;

    private final byte[]        m_iv;

    public AES(final char[] password, final byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException {

        // Derive the key, given password and salt
        final SecretKeyFactory factory = SecretKeyFactory
                .getInstance(SECRET_KEY_ALGORITHM);
        final KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS,
                KEY_LENGTH);
        final SecretKey tmp = factory.generateSecret(spec);
        final SecretKey secret = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

        // Build encryptor and get IV
        final Cipher enc_cipher = Cipher.getInstance(TRANSFORMATION);
        enc_cipher.init(Cipher.ENCRYPT_MODE, secret);
        final AlgorithmParameters params = enc_cipher.getParameters();
        final byte[] iv = params.getParameterSpec(IvParameterSpec.class)
                .getIV();

        // Build decryptor
        final Cipher dec_cipher = Cipher.getInstance(TRANSFORMATION);
        dec_cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

        this.m_enc_cipher = enc_cipher;
        this.m_dec_cipher = dec_cipher;

        this.m_iv = iv;
    }

    public AES(final byte[] iv) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            InvalidKeyException, InvalidParameterSpecException,
            IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        final AlgorithmParameterSpec aps = new IvParameterSpec(iv);

        final KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        keygen.init(KEY_LENGTH);
        final SecretKey secret = keygen.generateKey();

        // Build encryptor
        final Cipher enc_cipher = Cipher.getInstance(TRANSFORMATION);
        enc_cipher.init(Cipher.ENCRYPT_MODE, secret, aps);

        // Build decryptor
        final Cipher dec_cipher = Cipher.getInstance(TRANSFORMATION);
        dec_cipher.init(Cipher.DECRYPT_MODE, secret, aps);

        this.m_enc_cipher = enc_cipher;
        this.m_dec_cipher = dec_cipher;

        this.m_iv = iv;
    }

    public byte[] get_iv() {
        return this.m_iv;
    }

    public byte[] encrypt(final byte[] data) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            InvalidKeyException, InvalidParameterSpecException,
            IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {
        return this.m_enc_cipher.doFinal(data);
    }

    public byte[] decrypt(final byte[] data) throws IllegalBlockSizeException,
            BadPaddingException {
        return this.m_dec_cipher.doFinal(data);
    }
}
