import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;

final class PKCS8
{

  private static final ASN1ObjectIdentifier AES = ASN1ObjectIdentifier.getInstance(NISTObjectIdentifiers.id_aes128_CBC);

  static RSAPublicKey toPublic(RSAPrivateCrtKey pvt)
    throws GeneralSecurityException
  {
    RSAPublicKeySpec pub = new RSAPublicKeySpec(pvt.getModulus(), pvt.getPublicExponent());
    KeyFactory f = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) f.generatePublic(pub);
  }

  static byte[] encrypt(SecretKey secret, PrivateKey pvt)
    throws Exception
  {
    Cipher enc = Cipher.getInstance("AES/CBC/PKCS5Padding");
    enc.init(Cipher.WRAP_MODE, secret);
    ASN1Encodable params = new DEROctetString(enc.getIV());
    AlgorithmIdentifier algId = new AlgorithmIdentifier(AES, params);
    byte[] ciphertext = enc.wrap(pvt);
    return new EncryptedPrivateKeyInfo(algId, ciphertext).getEncoded();
  }

  static PrivateKey decrypt(SecretKey secret, byte[] pkcs8)
    throws Exception
  {
    EncryptedPrivateKeyInfo info = new PKCS8EncryptedPrivateKeyInfo(pkcs8).toASN1Structure();
    AlgorithmIdentifier id = info.getEncryptionAlgorithm();
    byte[] iv = ((ASN1OctetString) id.getParameters()).getOctets();
    Cipher dec = Cipher.getInstance("AES/CBC/PKCS5Padding");
    dec.init(Cipher.UNWRAP_MODE, secret, new IvParameterSpec(iv));
    return (PrivateKey) dec.unwrap(info.getEncryptedData(), "RSA", Cipher.PRIVATE_KEY);
  }

}
