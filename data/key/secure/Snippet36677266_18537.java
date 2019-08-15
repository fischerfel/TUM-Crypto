public static boolean decryptFileFromUri(Context context, Uri file, String keyphrase) {
    try {
        File f = new File(getRealPathFromURI(context, file));
        FileInputStream fis = new FileInputStream(f);

        File ef = new File(f.toString().replace(".epf", ""));
        FileOutputStream fos = new FileOutputStream(ef);

        Log.d("HIDEMYPICS","Decrypting: " + f.toString());

        byte[] rawKey = getRawKey(keyphrase.getBytes("UTF8"));
        /*KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(rawKey);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] key = skey.getEncoded();*/
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();

        Log.d("HIDEMYPICS","Decrypted to: " + ef.toString());
        return true;
    } catch (IOException e){
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    return false;
}

public static boolean encryptFileFromUri(Context context, Uri file, String keyphrase) {
    try {
        File f = new File(getRealPathFromURI(context, file));
        FileInputStream fis = new FileInputStream(f);

        File ef = new File(f.toString() + ".epf");
        FileOutputStream fos = new FileOutputStream(ef);

        Log.d("HIDEMYPICS","Encrypting: " + f.toString());

        byte[] rawKey = getRawKey(keyphrase.getBytes("UTF8"));
        /*KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(rawKey);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] key = skey.getEncoded();*/
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
        Log.d("HIDEMYPICS","Encrypted to: " + ef.toString());
        return true;
    } catch (IOException e){
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    return false;
}
