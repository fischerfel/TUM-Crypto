String iv = "fedcba9876543210";
IvParameterSpec ivspec;
KeyGenerator keygen;
Key key;

ivspec = new IvParameterSpec(iv.getBytes());

keygen = KeyGenerator.getInstance("AES");
keygen.init(128);
key = keygen.generateKey();

keyspec = new SecretKeySpec(key.getEncoded(), "AES"); 

Cipher cipher;
byte[] encrypted;

cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
encrypted = cipher.doFinal(padString(text).getBytes());

private String padString(String source) {
  char paddingChar = ' ';
  int size = 16;
  int padLength = size - source.length() % size;

  for (int i = 0; i < padLength; i++) {
    source += paddingChar;
  }

  return source;
}
