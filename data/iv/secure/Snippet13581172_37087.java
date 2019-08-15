public class AES4all {

public static Cipher getAESCBCEncryptor(byte[] keyBytes, byte[] IVBytes, String      padding) throws Exception{
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(IVBytes);
    Cipher cipher = Cipher.getInstance("AES/CBC/"+padding);
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
    return cipher;
}

public static Cipher getAESCBCDecryptor(byte[] keyBytes, byte[] IVBytes, String padding) throws Exception{
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(IVBytes);
    Cipher cipher = Cipher.getInstance("AES/CBC/"+padding);
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    return cipher; 
} 

public static Cipher getAESECBEncryptor(byte[] keyBytes, String padding) throws Exception{
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/"+padding);
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher;
}

public static Cipher getAESECBDecryptor(byte[] keyBytes, String padding) throws Exception{
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/"+padding);
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher;
}

public static byte[] encrypt(Cipher cipher, byte[] dataBytes) throws Exception{
    ByteArrayInputStream bIn = new ByteArrayInputStream(dataBytes);
    CipherInputStream cIn = new CipherInputStream(bIn, cipher);
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    int ch;
    while ((ch = cIn.read()) >= 0) {
      bOut.write(ch);
    }
    return bOut.toByteArray();
} 

public static byte[] decrypt(Cipher cipher, byte[] dataBytes) throws Exception{
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
    cOut.write(dataBytes);
    cOut.close();
    return bOut.toByteArray();    
} 
/**
 * @param args
 */

public static byte[] demo1encrypt(byte[] keyBytes, byte[] ivBytes, String sPadding, byte[] messageBytes) throws Exception {
    Cipher cipher = getAESCBCEncryptor(keyBytes, ivBytes, sPadding); 
    return encrypt(cipher, messageBytes);
}

public static byte[] demo1decrypt(byte[] keyBytes, byte[] ivBytes, String sPadding, byte[] encryptedMessageBytes) throws Exception {
    Cipher decipher = getAESCBCDecryptor(keyBytes, ivBytes, sPadding);
    return decrypt(decipher, encryptedMessageBytes);
}

public static byte[] demo2encrypt(byte[] keyBytes, String sPadding, byte[] messageBytes) throws Exception {
    Cipher cipher = getAESECBEncryptor(keyBytes, sPadding); 
    return encrypt(cipher, messageBytes);
}

public static byte[] demo2decrypt(byte[] keyBytes, String sPadding, byte[] encryptedMessageBytes) throws Exception {
    Cipher decipher = getAESECBDecryptor(keyBytes, sPadding);
    return decrypt(decipher, encryptedMessageBytes);
}

public static void main(String[] args) throws Exception {
    String sDemoMesage = "This is a demo message from Java!";
    byte[] demoMesageBytes = sDemoMesage.getBytes();
    //shared secret
    byte[] demoKeyBytes = new byte[] {  0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
    // Initialization Vector - usually a random data, stored along with the shared secret,
    // or transmitted along with a message.
    // Not all the ciphers require IV - we use IV in this particular sample
    byte[] demoIVBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                                    0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
    String sPadding = "ISO10126Padding"; //"ISO10126Padding", "PKCS5Padding"

    System.out.println("Demo Key (base64): "+ new String(SimpleBase64Encoder.encode(demoKeyBytes)));
    System.out.println("Demo IV  (base64): "+ new String(SimpleBase64Encoder.encode(demoIVBytes)));

    byte[] demo1EncryptedBytes = demo1encrypt(demoKeyBytes, demoIVBytes, sPadding, demoMesageBytes);
    System.out.println("Demo1 encrypted (base64): "+ new String(SimpleBase64Encoder.encode(demo1EncryptedBytes)));
    byte[] demo1DecryptedBytes = demo1decrypt(demoKeyBytes, demoIVBytes, sPadding, demo1EncryptedBytes);
    System.out.println("Demo1 decrypted message : "+new String(demo1DecryptedBytes));

    byte[] demo2EncryptedBytes = demo2encrypt(demoKeyBytes, sPadding, demoMesageBytes);
    System.out.println("Demo2 encrypted (base64): "+ new String(SimpleBase64Encoder.encode(demo2EncryptedBytes)));
    byte[] demo2DecryptedBytes = demo2decrypt(demoKeyBytes, sPadding, demo2EncryptedBytes);
    System.out.println("Demo2 decrypted message : "+new String(demo2DecryptedBytes));

}

}







 public class SimpleBase64Encoder {

// Mapping table from 6-bit nibbles to Base64 characters.
private static char[]    map1 = new char[64];
   static {
      int i=0;
      for (char c='A'; c<='Z'; c++) map1[i++] = c;
      for (char c='a'; c<='z'; c++) map1[i++] = c;
      for (char c='0'; c<='9'; c++) map1[i++] = c;
      map1[i++] = '+'; map1[i++] = '/'; }

// Mapping table from Base64 characters to 6-bit nibbles.
private static byte[]    map2 = new byte[128];
   static {
      for (int i=0; i<map2.length; i++) map2[i] = -1;
      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }

/**
* Encodes a string into Base64 format.
* No blanks or line breaks are inserted.
* @param s  a String to be encoded.
* @return   A String with the Base64 encoded data.
*/
public static String encodeString (String s) {
   return new String(encode(s.getBytes())); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted.
* @param in  an array containing the data bytes to be encoded.
* @return    A character array with the Base64 encoded data.
*/
public static char[] encode (byte[] in) {
   return encode(in,in.length); }

/**
* Encodes a byte array into Base64 format.
* No blanks or line breaks are inserted.
* @param in   an array containing the data bytes to be encoded.
* @param iLen number of bytes to process in <code>in</code>.
* @return     A character array with the Base64 encoded data.
*/
public static char[] encode (byte[] in, int iLen) {
   int oDataLen = (iLen*4+2)/3;       // output length without padding
   int oLen = ((iLen+2)/3)*4;         // output length including padding
   char[] out = new char[oLen];
   int ip = 0;
   int op = 0;
   while (ip < iLen) {
      int i0 = in[ip++] & 0xff;
      int i1 = ip < iLen ? in[ip++] & 0xff : 0;
      int i2 = ip < iLen ? in[ip++] & 0xff : 0;
      int o0 = i0 >>> 2;
      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
      int o3 = i2 & 0x3F;
      out[op++] = map1[o0];
      out[op++] = map1[o1];
      out[op] = op < oDataLen ? map1[o2] : '='; op++;
      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
   return out; }

/**
* Decodes a string from Base64 format.
* @param s  a Base64 String to be decoded.
* @return   A String containing the decoded data.
* @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
*/
public static String decodeString (String s) {
   return new String(decode(s)); }

/**
* Decodes a byte array from Base64 format.
* @param s  a Base64 String to be decoded.
* @return   An array containing the decoded data bytes.
* @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
*/
public static byte[] decode (String s) {
   return decode(s.toCharArray()); }

/**
* Decodes a byte array from Base64 format.
* No blanks or line breaks are allowed within the Base64 encoded data.
* @param in  a character array containing the Base64 encoded data.
* @return    An array containing the decoded data bytes.
* @throws    IllegalArgumentException if the input is not valid Base64 encoded data.
*/
public static byte[] decode (char[] in) {
   int iLen = in.length;
   if (iLen%4 != 0) throw new IllegalArgumentException ("Length of Base64 encoded input string is not a multiple of 4.");
   while (iLen > 0 && in[iLen-1] == '=') iLen--;
   int oLen = (iLen*3) / 4;
   byte[] out = new byte[oLen];
   int ip = 0;
   int op = 0;
   while (ip < iLen) {
      int i0 = in[ip++];
      int i1 = in[ip++];
      int i2 = ip < iLen ? in[ip++] : 'A';
      int i3 = ip < iLen ? in[ip++] : 'A';
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
      int b0 = map2[i0];
      int b1 = map2[i1];
      int b2 = map2[i2];
      int b3 = map2[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
      int o0 = ( b0       <<2) | (b1>>>4);
      int o1 = ((b1 & 0xf)<<4) | (b2>>>2);
      int o2 = ((b2 &   3)<<6) |  b3;
      out[op++] = (byte)o0;
      if (op<oLen) out[op++] = (byte)o1;
      if (op<oLen) out[op++] = (byte)o2; }
   return out; }

private SimpleBase64Encoder() {}

} // end class SimpleBase64Encoder
