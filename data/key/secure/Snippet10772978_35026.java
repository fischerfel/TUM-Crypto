public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
    encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
}

public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
    encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
}

private static byte[] getBytes(String toGet)
{
    try
    {
        byte[] retVal = new byte[toGet.length()];
        for (int i = 0; i < toGet.length(); i++)
        {
            char anychar = toGet.charAt(i);
            retVal[i] = (byte)anychar;
        }
        return retVal;
    }catch(Exception e)
    {
        String errorMsg = "ERROR: getBytes :" + e;
        return null;
    }
}

public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {


   String iv = "12345678";
   byte[] IVBytes = getBytes(iv);
   IvParameterSpec IV = new IvParameterSpec(IVBytes);


    byte[] KeyData = key.getBytes(); 
    SecretKeySpec blowKey = new SecretKeySpec(KeyData, "Blowfish"); 
    //Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
    Cipher cipher = Cipher.getInstance("Blowfish/CBC/NoPadding");

    if (mode == Cipher.ENCRYPT_MODE) {
        cipher.init(Cipher.ENCRYPT_MODE, blowKey, IV);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
    } else if (mode == Cipher.DECRYPT_MODE) {
        cipher.init(Cipher.DECRYPT_MODE, blowKey, IV);
        CipherOutputStream cos = new CipherOutputStream(os, cipher);
        doCopy(is, cos);
    }
}

public static void doCopy(InputStream is, OutputStream os) throws IOException {
    byte[] bytes = new byte[4096];
    //byte[] bytes = new byte[64];
    int numBytes;
    while ((numBytes = is.read(bytes)) != -1) {
        os.write(bytes, 0, numBytes);
    }
    os.flush();
    os.close();
    is.close();
}   

public static void main(String[] args) {


    //Encrypt the reports
    try {
        String key = "squirrel123";

        FileInputStream fis = new FileInputStream("war_and_peace.txt");
        FileOutputStream fos = new FileOutputStream("encrypted.txt");
        encrypt(key, fis, fos);

        FileInputStream fis2 = new FileInputStream("encrypted.txt");
        FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
        decrypt(key, fis2, fos2);
    } catch (Throwable e) {
        e.printStackTrace();
    }
}
