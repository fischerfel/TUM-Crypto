import codec.Base64;
import codec.CorruptedCodeException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
...
    Security.addProvider(new BouncyCastleProvider());

// Assume that we know the encoded public key then return it by decode the public key
    byte[] decodedPublic = Base64.decode("MEIwFAYHKoZIzj0CAQYJKyQDAwIIAQEBAyoABNXclcmtUt8/rlGN47pc8ZpxkWgNgtKeeHdsVD0kIWLUMEULnchGZPA=".getBytes());

    X509EncodedKeySpec formatted_public = new X509EncodedKeySpec(decodedPublic);
    KeyFactory kf = KeyFactory.getInstance("EC","BC");
    PublicKey pubKey = kf.generatePublic(formatted_public);

    Cipher c = Cipher.getInstance("ECIES", "BC");

    c.init(Cipher.ENCRYPT_MODE, pubKey); // How can i put the AES128_CBC for ies parameter ? is that possible

    byte[] cipher = c.doFinal("This is the message".getBytes());
    System.out.println("Ciphertext : "+ Base64.encode(cipher));
...
