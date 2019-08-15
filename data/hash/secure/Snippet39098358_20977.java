import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public void legacyEncryption(String salt, String clearPassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
  // Get digester instance for algorithm "SHA-512" using BounceCastle
  MessageDigest digester = MessageDigest.getInstance("SHA-512", new BouncyCastleProvider());

  // Create salted password string
  String mergedPasswordAndSalt = clearPassword + "{" + salt + "}";

  // First time hash the input string by using UTF-8 encoded bytes.
  byte[] hash = digester.digest(mergedPasswordAndSalt.getBytes("UTF-8"));

  // Loop 5k times
  for (int i = 0; i < 5000; i++) {
    // Concatenate the hash bytes with the clearPassword bytes and rehash
    hash = digester.digest(ArrayUtils.addAll(hash, mergedPasswordAndSalt.getBytes("UTF-8")));
  }

  // Log the resulting hash as base64 String
  logger.info("Legace password digest: salt=" + salt + " hash=" + Base64.getEncoder().encodeToString(hash));
}
