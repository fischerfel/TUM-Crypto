import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

public class ParseRsaPublicKey {

    public static void main(String[] args) throws Exception {
        String yourKeyB64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLz4yAo1FTOLc6nijBCTv5iVnW\n"+
                "F6MxCeM5+RxY+29AXcpMWhlM9oES3ESIfWw6OzrrENDyqwY+kVzCj2bWYnEAyJXs\n"+
                "WOpvqT2XSCPplwZOnQQGm+DnAYJXEeOfgU5DI63fwdiGv4M2ph1VMMe6684sBZu1\n"+
                "HhJHuhsX2eibBR0/lQIDAQAB";
        // create the key factory
        KeyFactory kFactory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        // decode base64 of your key
        byte yourKey[] =  Base64.decode(yourKeyB64);
        // generate the public key
        X509EncodedKeySpec spec =  new X509EncodedKeySpec(yourKey);
        PublicKey publicKey = (PublicKey) kFactory.generatePublic(spec);
        // now you can for example cipher some data with your key
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherData = cipher.doFinal("someData".getBytes());
        System.out.println(new String(cipherData));
    }

}
