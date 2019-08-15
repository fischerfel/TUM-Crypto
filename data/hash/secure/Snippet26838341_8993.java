public class Test {
public static void main(String...arg) throws IOException{
    System.out.println("First time");
    String string64 = getEncryptedPassword("FenilShah");
    System.out.println(string64);
    System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64(string64)));

    System.out.println("\nSecond time");
    string64 = getEncryptedPassword("FenilShah");
    System.out.println(string64);
    System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64(string64)));

    System.out.println("\nThird time");
    string64 = getEncryptedPassword("FenilShah");
    System.out.println(string64);
    System.out.println(StringUtils.newStringUtf8(Base64.decodeBase64(string64)));

}

 public static String getEncryptedPassword(String clearTextPassword)   {  


        try {
          MessageDigest md = MessageDigest.getInstance("SHA-256");
          md.update(clearTextPassword.getBytes());
          byte pass[] = md.digest();
          System.out.println(pass.toString());
          return Base64.encodeBase64String(StringUtils.getBytesUtf8(pass.toString()));
        } catch (NoSuchAlgorithmException e) {
          //_log.error("Failed to encrypt password.", e);
        }
        return "";
      }
 }
