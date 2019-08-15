public static InputStream decryptAES(Context context) {
    InputStream ris = null;

    try {
        InputStream fis = context.getAssets().open("somefile");
        FileOutputStream baos = new FileOutputStream("/sdcard/decrypted");
        String hash = "SOMEHASH";
        String ivs = "SOMEIV";

        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes("UTF-8"));
        SecretKeySpec sks = new SecretKeySpec(hash.getBytes("UTF-8"), "AES");

        // None of these work
        Cipher cipher = Cipher.getInstance("AES");
        //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //Cipher cipher = Cipher.getInstance("AES/ECB/ZeroBytePadding");
        //Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
        //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        //cipher.init(Cipher.DECRYPT_MODE, sks, iv);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[1024 * 32];
        while ((b = cis.read(d)) != -1) {
            baos.write(d, 0, b);
        }
        baos.flush();
        baos.close();
        cis.close();
    } catch (Exception e) {
        // Meh
    }

    return ris;
}
