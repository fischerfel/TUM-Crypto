import codec.Base64;
import codec.CorruptedCodeException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
...
    Security.addProvider(new BouncyCastleProvider());
// Assume that we know the encoded private key
    byte[] decodedPrivate = Base64.decode("MHECAQAwFAYHKoZIzj0CAQYJKyQDAwIIAQEBBFYwVAIBAQQUQmA9ifs472gNHBc5NSGYq56TlOKgCwYJKyQDAwIIAQEBoSwDKgAE1dyVya1S3z+uUY3julzxmnGRaA2C0p54d2xUPSQhYtQwRQudyEZk8A==".getBytes());

    PKCS8EncodedKeySpec formatted_private = new PKCS8EncodedKeySpec(decodedPrivate);
    KeyFactory kf = KeyFactory.getInstance("EC", "BC");
    PrivateKey privKey = kf.generatePrivate(formatted_private);

    Cipher c = Cipher.getInstance("ECIES");
    c.init(Cipher.DECRYPT_MODE, privKey); //How can i adding the **AES128_CBC** ies param ?

// Assume that we know the encoded cipher text
    byte[] plaintext = c.doFinal(Base64.decode("BKbCsKY7gDPld+F4jauQXvKSayYCt6vOjIGbsyXo5fHWo4Ax+Nt5BQ5FlkAGksFNRQ46agzfxjfuoxWkVfnt4gReDmpPYueUbiRiHp1Gwp0="));
    System.out.println("\nPlaintext : "+ new String (plaintext));
...
