public class App 
{
    static byte[] seckey=null;
    static
    {
        try
        {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
              kgen.init(128); 

                        // Generate the secret key specs.             
                        // SecretKey skey = kgen.generateKey();
                        // seckey = skey.getEncoded();
                        // above won't work as can't generate new secret key for decrypt. Have to use same key for encrypt and decrypt

                        // seckey = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
                        seckey = new byte[]{(byte)172,(byte)236,(byte)125,(byte)222,(byte)188,(byte)33,(byte)210,(byte)4,(byte)202,(byte)31,(byte)188,(byte)152,(byte)220,(byte)104,(byte)62,(byte)64};


        } catch (NoSuchAlgorithmException e)
        {           
            e.printStackTrace();
        }


    }
    public static void main( String[] args )
    {   

        String password = encrypt("A123456"); //working
        System.out.println(password);
        System.out.println(decrypt(password));

        String password = encrypt("A*501717"); //NOT working
        System.out.println(password);
        System.out.println(decrypt(password));

    }    
    public static String encrypt(String passwd)
    {
        SecretKeySpec key = new SecretKeySpec(seckey, "AES");
        byte[] output;
        try
        {
            Cipher cipher = Cipher.getInstance("AES");

            // encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key);
            output = cipher.doFinal(passwd.getBytes());
        } catch (Exception e)
        {
            System.out.println("Unable to encrypt password.");
            output = "".getBytes();
        }

        return new String(output);

    }

    public static String decrypt(String passwd)
    {
        if (!StringUtils.isNotBlank(passwd))
            return "";

            SecretKeySpec key = new SecretKeySpec(seckey, "AES");

        byte[] output;
        try
        {
            Cipher cipher = Cipher.getInstance("AES");
            // decryption pass
            cipher.init(Cipher.DECRYPT_MODE, key);
            output = cipher.doFinal(passwd.getBytes());
        } catch (Exception e)
        {
            System.out.println("Unable to decrypt password");
            output = "".getBytes();
        }

        return new String(output);

    }
   }
