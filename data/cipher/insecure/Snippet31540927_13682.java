import java.io.BufferedReader;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import javax.crypto.Cipher;
import org.bouncycastle.openssl.PEMReader;
import android.util.Base64;
import android.util.Log;

public class RsaEncryption {

private String publicKey;

public RsaEncryption(String publicKey)
{
    this.publicKey = publicKey;

}


/*
 * Function to encrypt the data.
 *
 */

public String encrypt( String data ) throws Exception
{



    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");


    byte[] keyBytes =   Base64.decode( this.publicKey, 0 );

    PublicKey publickey       = strToPublicKey(new String(keyBytes));
    cipher.init( Cipher.ENCRYPT_MODE , publickey );

    // Base 64 encode the encrypted data
    byte[] encryptedBytes = Base64.encode( cipher.doFinal(data.getBytes()), 0 );

    return new String(encryptedBytes);


}


public static PublicKey strToPublicKey(String s)
{

    PublicKey pbKey = null;
    try {

        BufferedReader br   = new BufferedReader( new StringReader(s) );
        PEMReader pr        = new PEMReader(br);
        Object obj = pr.readObject();

        if( obj instanceof PublicKey )
        {
            pbKey = (PublicKey) pr.readObject();
        }
        else if( obj instanceof KeyPair )
        {
            KeyPair kp = (KeyPair) pr.readObject();
            pbKey = kp.getPublic();
        }
        pr.close();

    }
    catch( Exception e )
    {
        Log.d("CIPHER", e.getMessage() );
    }

    return pbKey;
}

}
