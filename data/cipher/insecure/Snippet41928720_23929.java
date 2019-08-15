@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
private static final byte[] KEY = "MySuperSecretKey".getBytes();

@Override
public String convertToDatabaseColumn(String ccNumber) {
  // do some encryption
  Key key = new SecretKeySpec(KEY, "AES");
  try {
     Cipher c = Cipher.getInstance(ALGORITHM);
     c.init(Cipher.ENCRYPT_MODE, key);
     return Base64.encodeBytes(c.doFinal(ccNumber.getBytes()));
  } catch (Exception e) {
     throw new RuntimeException(e);
  }
}

@Override
public String convertToEntityAttribute(String dbData) {
  // do some decryption
  Key key = new SecretKeySpec(KEY, "AES");
  try {
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    return new String(c.doFinal(Base64.decode(dbData)));
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}
}
