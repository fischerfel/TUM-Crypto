public static void DecryptLIGGGHTSInputFile(String fileIn, String fileOut, String base64Key, String base64IV) throws Exception
{

    // Get the keys from base64 text
    byte[] key = Base64.decodeBase64(base64Key);
    byte[] iv= Base64.decodeBase64(base64IV);

    // Read fileIn into a byte[]
    int len = (int)(new File(fileIn).length());
    byte[] cipherText = new byte[len];
    FileInputStream bs = new FileInputStream(fileIn);
    bs.read(cipherText, 1, len-1);
    System.out.println(cipherText.length);
    System.out.println((double)cipherText.length/128);
    bs.close();

    // Create an Aes object 
    // with the specified key and IV. 
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    // Encrypt the message. 
    SecretKey secret = new SecretKeySpec(key, "AES");

    /*
    cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
    cipherText = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
    System.out.println(cipherText);
    */

    cipher.init(Cipher.DECRYPT_MODE, secret , new IvParameterSpec(iv));
    String plaintext = new String(cipher.doFinal(cipherText), "UTF-8");
    System.out.println(plaintext.length());


    FileWriter fw = new FileWriter(fileOut);
    fw.write(plaintext);
    fw.close();
}
