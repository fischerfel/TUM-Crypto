import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by sergi.castellsague on 04/05/2014.
 */
public class SecurityManager
{
private static final int ITERATIONS = 1000;

public enum HashMethod
{
    PBKDF2(){
        @Override
        public String getHashString()
        {
            return "PBKDF2WithHmacSHA1";
        }
    }, SHA512(){
        @Override
        public String getHashString() {
            return "SHA-512";
        }
    }, SHA384() {
        @Override
        public String getHashString() {
            return "SHA-384";
        }
    }, SHA256() {
        @Override
        public String getHashString () {
            return "SHA-256";
        }
    }
    , SHA1()
    {
        @Override
        public String getHashString() {
            return "SHA-1";
        }
    };

    public abstract String getHashString();

}

public static HashMethod getAppropriateHash()
{
    HashMethod method = null;

    if ( isPBKDFAvailable() )
    {
        method = HashMethod.PBKDF2;
    }
    else if( isDigestAvailable( HashMethod.SHA512.getHashString() ) )
    {
        method = HashMethod.SHA512;
    }
    else if( isDigestAvailable( HashMethod.SHA384.getHashString() ) )
    {
        method = HashMethod.SHA384;
    }
    else if( isDigestAvailable( HashMethod.SHA256.getHashString() ) )
    {
        method = HashMethod.SHA256;
    }
    else if( isDigestAvailable( HashMethod.SHA1.getHashString() ) )
    {
        method = HashMethod.SHA1;
    }

    return method;
}


private static boolean isPBKDFAvailable()
{
    try
    {
        SecretKeyFactory.getInstance( HashMethod.PBKDF2.getHashString() );
    }
    catch ( Exception notAvailable)
    {
        return false;
    }
    return true;
}

private static boolean isDigestAvailable( String method )
{
    try
    {
        MessageDigest.getInstance( method );
    }
    catch ( Exception notAvailable )
    {
        return false;
    }

    return true;
}

public static String getHashedPassword( HashMethod method, String password )
{
    String hashed;

    if ( HashMethod.PBKDF2.getHashString().equals( method.getHashString() ) )
    {
        hashed = generatePBKDF( password );
    }
    else
    {
        hashed = password;
        for ( int i = 0; i < ITERATIONS; i++ )
        {
            hashed = generateDigestPassword( password, method.getHashString() );
        }
    }

    return hashed;
}

private static String generatePBKDF( String password )
{
    // Generate a 512-bit key
    final int outputKeyLength = 512;

    char[] chars = new char[password.length()];
    password.getChars( 0, password.length(), chars, 0 );
    byte[] salt = "salt_on_client_is_funny".getBytes(); // In security terms, this is worthess. However, it's required.

    byte[] hashedPassBytes = new byte[0];
    try
    {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance( HashMethod.PBKDF2.getHashString() );
        KeySpec keySpec = new PBEKeySpec( chars, salt, ITERATIONS, outputKeyLength );

        hashedPassBytes = secretKeyFactory.generateSecret( keySpec ).getEncoded();
    }
    catch ( Exception shouldNotHappen )
    {}

    return Base64.encodeToString( hashedPassBytes, Base64.DEFAULT );
}

private static String generateDigestPassword( String password, String algorithm )
{
    byte[] digest = new byte[0];
    byte[] buffer = password.getBytes();

    try {
        MessageDigest messageDigest = MessageDigest.getInstance( algorithm );
        messageDigest.reset();
        messageDigest.update( buffer );
        digest = messageDigest.digest();
    }
    catch ( NoSuchAlgorithmException ex )
    {}

    return Base64.encodeToString(digest, Base64.DEFAULT);
}
}
