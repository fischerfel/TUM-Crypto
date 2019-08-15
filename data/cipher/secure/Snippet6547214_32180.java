public String encrypt(String text) throws Exception
{
    if(text == null || text.length() == 0)
        throw new Exception("Empty string");

    Cipher cipher;
    byte[] encrypted = null;

    try {
        cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

        encrypted = cipher.doFinal(padString(text).getBytes());
    } catch (Exception e)
    {           
        throw new Exception("[encrypt] " + e.getMessage());
    }

    return new String( encrypted );
}

public String decrypt(String code) throws Exception
{
    if(code == null || code.length() == 0)
        throw new Exception("Empty string");

    Cipher cipher;
    byte[] decrypted = null;

    try {
        cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        decrypted = cipher.doFinal(hexToBytes(code));
    } catch (Exception e)
    {
        throw new Exception("[decrypt] " + e.getMessage());
    }
    return new String( decrypted );
}


private static byte[] hexToBytes(String hex) {
  String HEXINDEX = "0123456789abcdef";
  int l = hex.length() / 2;
  byte data[] = new byte[l];
  int j = 0;

  for (int i = 0; i < l; i++) {
    char c = hex.charAt(j++);
    int n, b;

    n = HEXINDEX.indexOf(c);
    b = (n & 0xf) << 4;
    c = hex.charAt(j++);
    n = HEXINDEX.indexOf(c);
    b += (n & 0xf);
    data[i] = (byte) b;
  }

  return data;
}

private static String padString(String source)
{
  char paddingChar = ' ';
  int size = 16;
  int x = source.length() % size;
  int padLength = size - x;

  for (int i = 0; i < padLength; i++)
  {
      source += paddingChar;
  }

  return source;
}
