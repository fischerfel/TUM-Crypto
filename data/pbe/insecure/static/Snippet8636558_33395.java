PBEKeySpec pbeKeySpec;
PBEParameterSpec paramSpec;
SecretKeyFactory keyFac;
byte[] salt = {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
               (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99};
int count = 20;
paramSpec = new PBEParameterSpec(salt, count);

try {
    pbeKeySpec = new PBEKeySpec("my_password".toCharArray(), salt, count);
    SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(pbeKeySpec);

    encCipher = Cipher.getInstance(secretKey.getAlgorithm());
    encCipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
    decCipher = Cipher.getInstance(secretKey.getAlgorithm());
    decCipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

} catch (Exception ex) {
    ex.printStackTrace();
 }
