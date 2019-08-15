import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
public class prueba {
/**
* @param args
* @throws NoSuchProviderException 
* @throws NoSuchAlgorithmException 
* @throws InvalidKeyException 
* @throws SignatureException 
* @throws NoSuchPaddingException 
* @throws BadPaddingException 
* @throws IllegalBlockSizeException 
*///
public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
// TODO Auto-generated method stub
KeyPair keyPair = KeyPairGenerator.getInstance("RSA","BC").generateKeyPair();
PrivateKey privateKey = keyPair.getPrivate();
PublicKey puKey = keyPair.getPublic();
String plaintext = "This is the message being signed";
// Hacer la firma
Signature instance = Signature.getInstance("SHA1withRSA","BC");
instance.initSign(privateKey);
instance.update((plaintext).getBytes());
byte[] signature = instance.sign();
// En dos partes primero hago un Hash
MessageDigest digest = MessageDigest.getInstance("SHA1", "BC");
byte[] hash = digest.digest((plaintext).getBytes());
// El digest es identico a  openssl dgst -sha1 texto.txt
//MessageDigest sha1 = MessageDigest.getInstance("SHA1","BC");
//byte[] digest = sha1.digest((plaintext).getBytes());
AlgorithmIdentifier digestAlgorithm = new AlgorithmIdentifier(new
DERObjectIdentifier("1.3.14.3.2.26"), null);
// create the digest info
DigestInfo di = new DigestInfo(digestAlgorithm, hash);
byte[] digestInfo = di.getDEREncoded();
//Luego cifro el hash
Cipher cipher = Cipher.getInstance("RSA","BC");
cipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] cipherText = cipher.doFinal(digestInfo);
//byte[] cipherText = cipher.doFinal(digest2);
Cipher cipher2 = Cipher.getInstance("RSA","BC");
cipher2.init(Cipher.DECRYPT_MODE, puKey);
byte[] cipherText2 = cipher2.doFinal(signature);
System.out.println("Input data: " + plaintext);
System.out.println("Digest: " + bytes2String(hash));
System.out.println("Signature: " + bytes2String(signature));
System.out.println("Signature2: " + bytes2String(cipherText));
System.out.println("DigestInfo: " + bytes2String(digestInfo));
System.out.println("Signature Decipher: " + bytes2String(cipherText2));
}
