    public class cipher {
       public static void main (String[] args) throws GeneralSecurityException{
          try {
             FileInputStream keyfis = new FileInputStream(args[0]);
             byte[] encKey = new byte[keyfis.available()];  
             System.out.println(encKey);
             keyfis.read(encKey);
             System.out.println(encKey);
             System.out.println("1");
             X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encKey);
             System.out.println(keySpec);
              KeyFactory kf = KeyFactory.getInstance("RSA");
             System.out.println("2");
             PublicKey pubKey = kf.generatePublic(keySpec);
              System.out.println("3r");
             Cipher cipher = Cipher.getInstance("RSA");
             cipher.init(Cipher.ENCRYPT_MODE,pubKey);   
              System.out.println("4");
               }


         catch(Exception e)
           {}


        }   


}
