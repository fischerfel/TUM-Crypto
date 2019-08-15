public class MainActivity {
    public static void main(String[] args) {

    Key publicKey = null;
    Key privateKey = null;
    byte[] encoded;
    byte[] text = new byte[0];

    try {
      text = "This is my secret message".getBytes();

      Cipher c = Cipher.getInstance("RSA");
      c.init(Cipher.ENCRYPT_MODE, publicKey);
      encoded = c.doFinal(text);

      c = Cipher.getInstance("RSA");
      c.init(Cipher.DECRYPT_MODE, privateKey);
      text = c.doFinal(encoded);

      } catch (Exception e) {
        System.out.println("Exception encountered. Exception is " +    e.getMessage());
      }
      System.out.println(String.valueOf(text)); //get random values here
    }
  }
