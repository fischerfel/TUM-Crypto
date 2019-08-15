import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.Security;

public class JCETest {
public static void main( String[] args ) throws GeneralSecurityException
{

    BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
    Security.addProvider(bouncyCastleProvider);

    System.out.println( "\nSecurity-Provider:" );
    for( Provider prov : Security.getProviders() ) {
        System.out.println( "  " + prov + ": " + prov.getInfo() );
    }
    System.out.println( "\nMaxAllowedKeyLength (for '" + Cipher.getInstance("AES").getProvider() + "' using current 'JCE Policy Files'):\n"
            + "  DES        = " + Cipher.getMaxAllowedKeyLength( "DES"        ) + "\n"
            + "  Triple DES = " + Cipher.getMaxAllowedKeyLength( "Triple DES" ) + "\n"
            + "  AES        = " + Cipher.getMaxAllowedKeyLength( "AES"        ) + "\n"
            + "  Blowfish   = " + Cipher.getMaxAllowedKeyLength( "Blowfish"   ) + "\n"
            + "  RSA        = " + Cipher.getMaxAllowedKeyLength( "RSA"        ) + "\n" );
}
}
