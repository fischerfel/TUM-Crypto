public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    try
    {

        //Prompt for String
        System.out.print("Enter a sentence:");
        String in = input.next();

        //Generate Key for encryption/decryption
        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey myDesKey = keygenerator.generateKey();
        Cipher desCipher;
        desCipher = Cipher.getInstance("DES");
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

        //Cast the input into bytes 
        byte[] text = in.getBytes();
        System.out.println("" + new String(text));
        // Encrypt the text
        byte[] textEncrypted = desCipher.doFinal(text);
        System.out.println("File Encryted : " + textEncrypted);
        // Initialize the same cipher for decryption
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        // Decrypt the text
        byte[] textDecrypted = desCipher.doFinal(textEncrypted);
        System.out.println("File Decryted : " +  new String(textDecrypted));

    }catch(NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }catch(NoSuchPaddingException e)
    {
        e.printStackTrace();
    }catch(InvalidKeyException e)
    {
        e.printStackTrace();
    }catch(IllegalBlockSizeException e)
    {
        e.printStackTrace();
    }catch(BadPaddingException e)
    {
        e.printStackTrace();
    } 

}
