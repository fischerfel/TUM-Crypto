public class PasswordCrypter {

    private Key key;
    public PasswordCrypter(String password)  {

              try{
                    KeyGenerator generator;
                    generator = KeyGenerator.getInstance("DES");
                    SecureRandom sec = new SecureRandom(password.getBytes());
                    generator.init(sec);
                    key = generator.generateKey();
              }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public byte[] encrypt(byte[] array) throws CrypterException {

        try{
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

                return cipher.doFinal(array);
        }catch (Exception e) {  
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] array) throws CrypterException{

        try{
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return cipher.doFinal(array);
        }catch(Exception e ){
            e.printStackTrace();
        }


        return null;
    }
}
