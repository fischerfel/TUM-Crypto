public class Cripthografy {
private static String TAG="freeliberomail";

 public static String encrypt(String seed, String cleartext){
   byte[] rawKey;
   byte[] result=null;
 try {
rawKey = getRawKey(seed.getBytes());

     result = encrypt(rawKey, cleartext.getBytes());
  } catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
  }
   return toHex(result);
  }

  public static String decrypt(String seed, String encrypted) {
    byte[] rawKey;
    byte[] result=null;
   try {
rawKey = getRawKey(seed.getBytes());

    byte[] enc = toByte(encrypted);
    result = decrypt(rawKey, enc);
    } catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
   }
    return new String(result);
   }


   private static byte[] getRawKey(byte[] seed) throws Exception {
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      sr.setSeed(seed);
      kgen.init(128, sr); // 192 and 256 bits may not be available
      SecretKey skey = kgen.generateKey();
      byte[] raw = skey.getEncoded();
      return raw;
    }
    private static byte[] encrypt(byte[] raw, byte[] clear){
 byte[] encrypted=null;
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher;
     try {
 cipher = Cipher.getInstance("AES");
     cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
     encrypted = cipher.doFinal(clear);
     } catch (NoSuchAlgorithmException e) {
// TODO Auto-generated catch block
e.printStackTrace();
    } catch (NoSuchPaddingException e) {
// TODO Auto-generated catch block
e.printStackTrace();
    } catch (InvalidKeyException e) {
// TODO Auto-generated catch block
e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
// TODO Auto-generated catch block
e.printStackTrace();
    } catch (BadPaddingException e) {
// TODO Auto-generated catch block
e.printStackTrace();
    }
     return encrypted;
    }


     private static byte[] decrypt(byte[] raw, byte[] encrypted) {
 byte[] decrypted = null;
     SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
     Cipher cipher;
     try {
 cipher = Cipher.getInstance("AES");
     cipher.init(Cipher.DECRYPT_MODE, skeySpec);
     decrypted = cipher.doFinal(encrypted);
     }catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvalidKeyException e) {
// TODO Auto-generated catch block
e.printStackTrace(); 
     } catch (IllegalBlockSizeException e) {
// TODO Auto-generated catch block
e.printStackTrace();
   } catch (BadPaddingException e) {
// TODO Auto-generated catch block
e.printStackTrace();
   }

    return decrypted;
   }


  public static String toHex(String txt) {
  return toHex(txt.getBytes());
  }

 public static String fromHex(String hex) {
 return new String(toByte(hex));
 }

 public static byte[] toByte(String hexString) {
  int len = hexString.length()/2;
  byte[] result = new byte[len];
  for (int i = 0; i < len; i++)
  result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
  return result;
 }


  public static String toHex(byte[] buf) {
    if (buf == null)
    return "";
    StringBuffer result = new StringBuffer(2*buf.length);
      for (int i = 0; i < buf.length; i++) {
        appendHex(result, buf[i]);
    }
     return result.toString();
   }
  private final static String HEX = "0123456789ABCDEF";
  private static void appendHex(StringBuffer sb, byte b) {
  sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
  }

}
