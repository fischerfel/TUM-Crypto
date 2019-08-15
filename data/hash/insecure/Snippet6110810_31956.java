// codes array
char[] codes = new char[64];

// initialise
private void initCodes(){
  codes = new char[64];
  codes[0] = '$';
  int count = 0;
  for (char i='0';i<='9';i++){ count++; codes[count] = i; }
  for (char i='A';i<='Z';i++){ count++; codes[count] = i; }
  for (char i='a';i<='z';i++){ count++; codes[count] = i; }
  codes[63] = '£';
}

// custom MD5 algorithm
public String customMD5(String source) {
  initCodes();
  byte[] buf = new byte[source.length()];
  buf = source.getBytes();
  MessageDigest algorithm = null;
  try {
    algorithm = MessageDigest.getInstance("MD5");
  } catch(NoSuchAlgorithmException e){}
  algorithm.reset();
  algorithm.update(buf);
  byte[] digest = algorithm.digest();
  int len = digest.length;
  char[] encrypted = new char[len];
  for (int i=0;i<len;i++)
    encrypted[i] = codes[(int)(Math.floor((double)((digest[i]+128)/4)))];
  return new String(encrypted);
}
