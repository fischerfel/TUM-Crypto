import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;


public static final String RSAKeyFactory = "RSA";
public static final String RSAKeyAlgorithm = "RSA/ECB/PKCS1Padding";
public static final String UTF_8 = "UTF-8";

byte[] mod = Base64.getDecoder().decode("3dlF3Frvwmuet+gM/LX8EQBI...");
BigInteger modulus = new BigInteger(1,mod);

byte[] prive64 = Base64.getDecoder().decode("YwTJhmqOS58PJzhhvuREI...");
BigInteger privExp = new BigInteger(1,prive64);

KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PrivateKey privateKey = keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, privExp));

byte[] cipherBytes = Base64.getDecoder().decode("VdznCyJeYukJahmHsbge...");
Cipher cipher = Cipher.getInstance(RSAKeyAlgorithm);
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] plainData = cipher.doFinal(cipherBytes);
