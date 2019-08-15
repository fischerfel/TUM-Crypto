public static SecretKey generateKey(Context c, char[] passphraseOrPin) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
    byte[] salt = Settings.Secure.getString(c.getContentResolver(),
            Settings.Secure.ANDROID_ID).getBytes();

    final int iterations = 1000;

    final int outputKeyLength = 128;

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    Log.d("HIDEMYPICS","Secret Key: " + toHex(secretKey.getEncoded()) );
    return secretKey;
}

public static boolean decryptFileFromUri(Context context, Uri file, String keyphrase) {
    try {
        File f = new File(getRealPathFromURI(context, file));
        FileInputStream fis = new FileInputStream(f);

        File ef = new File(f.toString().replace(".epf", ""));
        FileOutputStream fos = new FileOutputStream(ef);

        Log.d("HIDEMYPICS","Decrypting: " + f.toString());

        SecretKey key = generateKey(context, keyphrase.toCharArray());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
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
    } catch (InvalidKeySpecException e) {
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

        SecretKey key = generateKey(context, keyphrase.toCharArray());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
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
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }
    return false;
}
