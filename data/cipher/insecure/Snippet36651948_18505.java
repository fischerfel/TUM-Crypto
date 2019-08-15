public class EncryptionUtils {
    private static SecretKeySpec skeySpec;

    static {    
        try {           
            ClassPathResource res = new ClassPathResource("key.key");
            if(res != null){
                File file = res.getFile();
                FileInputStream input = new FileInputStream(file);
                byte[] in = new byte[(int)file.length()];
                input.read(in);
                skeySpec = new SecretKeySpec(in, "AES");
                input.close();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static byte[] encrypt(byte[] input) 
            throws GeneralSecurityException, NoSuchPaddingException{
           Cipher cipher = Cipher.getInstance("AES");

           cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
           return cipher.doFinal(input);

    }


    public static byte[] decrypt(byte[] input) throws GeneralSecurityException, NoSuchPaddingException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(input);
    }

}
