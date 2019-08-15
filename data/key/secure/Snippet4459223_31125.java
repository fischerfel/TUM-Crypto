import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.Key;
import java.text.NumberFormat;
public static void runEncTest( byte[] encryptionKey, byte[] ivBytes, byte[] input )
{
  try
  {
    Key key = new SecretKeySpec( encryptionKey, "AES" );

    Cipher cipher = Cipher.getInstance( "AES/CTS/NoPadding" );
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));

    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
    ctLength += cipher.doFinal(cipherText, ctLength);

    printByteArray( "PLAIN:", input );
    printByteArray( "CRYPT:", cipherText );      
  }
  catch( java.lang.Exception e )
  {
    System.out.println("Got an Exception: " + e.getMessage());
  }
}
