import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Util class to perform encryption/decryption over strings. <br/>
 */
public final class UtilsEncryption
{
    /** The logging TAG */
    private static final String TAG = UtilsEncryption.class.getName();

    /** */
    private static final String KEY = "some_encryption_key";

    /**
     * Avoid instantiation. <br/>
     */
    private UtilsEncryption()
    {
    }

    /** The HEX characters */
    private final static String HEX = "0123456789ABCDEF";

    /**
     * Encrypt a given string. <br/>
     * 
     * @param the string to encrypt
     * @return the encrypted string in HEX
     */
    public static String encrypt( String cleartext )
    {
        try
        {
            byte[] result = process( Cipher.ENCRYPT_MODE, cleartext.getBytes() );
            return toHex( result );
        }
        catch ( Exception e )
        {
            System.out.println( TAG + ":encrypt:" + e.getMessage() );
        }
        return null;
    }

    /**
     * Decrypt a HEX encrypted string. <br/>
     * 
     * @param the HEX string to decrypt
     * @return the decrypted string
     */
    public static String decrypt( String encrypted )
    {
        try
        {
            byte[] enc = fromHex( encrypted );
            byte[] result = process( Cipher.DECRYPT_MODE, enc );
            return new String( result );
        }
        catch ( Exception e )
        {
            System.out.println( TAG + ":decrypt:" + e.getMessage() );
        }
        return null;
    }


    /**
     * Get the raw encryption key. <br/>
     * 
     * @param the seed key
     * @return the raw key
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getRawKey()
        throws NoSuchAlgorithmException
    {
        KeyGenerator kgen = KeyGenerator.getInstance( "AES" );
        SecureRandom sr = SecureRandom.getInstance( "SHA1PRNG" );
        sr.setSeed( KEY.getBytes() );
        kgen.init( 128, sr );
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    /**
     * Process the given input with the provided mode. <br/>
     * 
     * @param the cipher mode
     * @param the value to process
     * @return the processed value as byte[]
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    private static byte[] process( int mode, byte[] value )
        throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,     NoSuchAlgorithmException,
        NoSuchPaddingException
    {
        SecretKeySpec skeySpec = new SecretKeySpec( getRawKey(), "AES" );
        Cipher cipher = Cipher.getInstance( "AES" );
        cipher.init( mode, skeySpec );
        byte[] encrypted = cipher.doFinal( value );
        return encrypted;
    }

    /**
     * Decode an HEX encoded string into a byte[]. <br/>
     * 
     * @param the HEX string value
     * @return the decoded byte[]
     */
    protected static byte[] fromHex( String value )
    {
        int len = value.length() / 2;
        byte[] result = new byte[len];
        for ( int i = 0; i < len; i++ )
        {
            result[i] = Integer.valueOf( value.substring( 2 * i, 2 * i + 2 ), 16     ).byteValue();
        }
        return result;
    }

    /**
     * Encode a byte[] into an HEX string. <br/>
     * 
     * @param the byte[] value
     * @return the HEX encoded string
     */
    protected static String toHex( byte[] value )
    {
        if ( value == null )
        {
            return "";
        }
        StringBuffer result = new StringBuffer( 2 * value.length );
        for ( int i = 0; i < value.length; i++ )
        {
            byte b = value[i];

            result.append( HEX.charAt( ( b >> 4 ) & 0x0f ) );
            result.append( HEX.charAt( b & 0x0f ) );
        }
        return result.toString();
    }
}
