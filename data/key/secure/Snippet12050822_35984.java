/**
 * Encryption of a given text using the provided secretKey
 * 
 * @param text
 * @param secretKey
 * @return the encoded string
 * @throws SignatureException
 */
public static String hashMac(String text, String secretKey)
  throws SignatureException {

 try {
  Key sk = new SecretKeySpec(secretKey.getBytes(), HASH_ALGORITHM);
  Mac mac = Mac.getInstance(sk.getAlgorithm());
  mac.init(sk);
  final byte[] hmac = mac.doFinal(text.getBytes());
  return toHexString(hmac);
 } catch (NoSuchAlgorithmException e1) {
  // throw an exception or pick a different encryption method
  throw new SignatureException(
    "error building signature, no such algorithm in device "
      + HASH_ALGORITHM);
 } catch (InvalidKeyException e) {
  throw new SignatureException(
    "error building signature, invalid key " + HASH_ALGORITHM);
 }
}
