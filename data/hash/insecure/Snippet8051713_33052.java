//First method call uses a swing component to get the user entered password.
String password = getPassword(); 

//This line is where the work starts with the second and third methods below.
String hashed = byteToBase64(getHash(password));

//The second method call here gets the encryption.
public static byte[] getHash(String password) {
      MessageDigest digest = null;
      byte[] input = null;
      try {
             digest = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException e1) {
             e1.printStackTrace();
      }
      digest.reset();
      try {
             input = digest.digest(password.getBytes("UTF-8"));
      } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
      }
      return input;
}

//Then the third method call here gets the encoding, FROM THE ENCRYPTED STRING.
public static String byteToBase64(byte[] data){
    return new String(Base64.encodeBase64(data));
