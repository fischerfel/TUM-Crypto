public class Crypt {

    public static void main(String[] args) {

        try{
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();

            Cipher desalgCipher;
            desalgCipher = Cipher.getInstance("DES");


            byte[] text = "test".getBytes("UTF8");


            desalgCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            byte[] textEncrypted = desalgCipher.doFinal(text);

            String s = new String(textEncrypted);
            System.out.println(s);

            desalgCipher.init(Cipher.DECRYPT_MODE, myDesKey);
            byte[] textDecrypted = desalgCipher.doFinal(textEncrypted);

            s = new String(textDecrypted);
            System.out.println(s);
        }

            catch(Exception e)
            {
                System.out.println("Error");
            }
    }

}
