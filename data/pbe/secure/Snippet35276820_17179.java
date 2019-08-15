import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public String decrypt(String keyDataStr, String passwordStr){
  // This key data start from "X5... to ==" 
  char [] password=passwordStr.toCharArray();
  byte [] keyDataBytes=com.sun.jersey.core.util.Base64.decode(keyDataStr);

  PBEKeySpec pbeSpec = new PBEKeySpec(password);
  EncryptedPrivateKeyInfo pkinfo = new EncryptedPrivateKeyInfo(keyDataBytes);
  SecretKeyFactory skf = SecretKeyFactory.getInstance(pkinfo.getAlgName());
  Key secret = skf.generateSecret(pbeSpec);
  PKCS8EncodedKeySpec keySpec = pkinfo.getKeySpec(secret);
  KeyFactory kf = KeyFactory.getInstance("RSA");
  PrivateKey pk=kf.generatePrivate(keySpec);
  return pk.toString();
}
