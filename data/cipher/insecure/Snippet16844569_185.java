   public byte[]   getImageFile(String fileName) throws FileNotFoundException
{
  byte[] Image_data = null;
  byte[] inarry = null;

    try {
        File file = new File(fileName);
        @SuppressWarnings("resource")
    FileInputStream  is = new FileInputStream (file); // use recorded file instead of getting file from assets folder.
        int length = is.available();
        Image_data = new byte[length];

        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = is.read(Image_data)) != -1)
        {
            output.write(Image_data, 0, bytesRead);
        }
      inarry = output.toByteArray();

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

return inarry;
}


public  byte[] encrypt(String seed, byte[] cleartext) throws Exception {

    byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext);
      //  return toHex(result);
        return result;
}

public  byte[] decrypt(String seed, byte[] encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = encrypted;
        byte[] result = decrypt(rawKey, enc);

        return result;
}

//done
private  byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
    kgen.init(128, sr); 
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
} 


private  byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
}

private  byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
}
