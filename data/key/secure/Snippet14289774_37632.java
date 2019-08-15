public static void decrypt() {
    try {
        Log.d(C.TAG, "Decrypt Started");

        byte[] bytes = new BigInteger(<your key>, 16).toByteArray();

        FileInputStream fis = new FileInputStream(<location of encrypted file>);

        FileOutputStream fos = new FileOutputStream(<location of decrypted file>);
        SecretKeySpec sks = new SecretKeySpec(bytes, <encryption type>);
        Cipher cipher = Cipher.getInstance(<encryption type>);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
        Log.d(C.TAG, "Decrypt Ended");
    } catch (NoSuchAlgorithmException e) {
        Log.d(C.TAG, "NoSuchAlgorithmException");
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        Log.d(C.TAG, "InvalidKeyException");
        e.printStackTrace();
    } catch (IOException e) {
        Log.d(C.TAG, "IOException");
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        Log.d(C.TAG, "NoSuchPaddingException");
        e.printStackTrace();
    }
}
