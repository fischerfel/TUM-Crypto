public class en {
    public static void main(String[] args){
      ...
    try{
      System.out.print("Enter text: ");
        String text = dataIn.readLine();
        String key = "dAtAbAsE98765432"; // 128 bit key

     // Create key and cipher
     Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
     Cipher cipher = Cipher.getInstance("AES");

     // encrypt the text
     cipher.init(Cipher.ENCRYPT_MODE, aesKey);
     byte[] encrypted = cipher.doFinal(text.getBytes());
     System.err.println("Encrypted: " + new String(encrypted));

     // Decrypt the text
     cipher.init(Cipher.DECRYPT_MODE, aesKey);
     String decrypted = new String(cipher.doFinal(encrypted));
     System.err.println("Decrypted: " + decrypted);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
