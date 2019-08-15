public String encrypt(String challenge) {
    try {
        KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        mKeyStore.load(null);
        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(mAlias, null);
        Cipher cip = null;
        cip = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cip.init(Cipher.ENCRYPT_MODE, entry.getPrivateKey());
        byte[] encryptBytes = cip.doFinal(challenge.getBytes());
        String encryptedStr64 = Base64.encodeToString(encryptBytes, Base64.NO_WRAP);
        return encryptedStr64;
    } catch (NoSuchAlgorithmException e) {
        Log.w(TAG, "No Such Algorithm Exception");
        e.printStackTrace();
    } catch (UnrecoverableEntryException e) {
        Log.w(TAG, "Unrecoverable Entry Exception");
        e.printStackTrace();
    } catch (KeyStoreException e) {
        Log.w(TAG, "KeyStore Exception");
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        Log.w(TAG, "Invalid Key Exception");
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        Log.w(TAG, "No Such Padding Exception");
        e.printStackTrace();
    } catch (BadPaddingException e) {
        Log.w(TAG, "Bad Padding Exception");
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        Log.w(TAG, "Illegal Block Size Exception");
        e.printStackTrace();
    } catch (CertificateException e) {
        Log.w(TAG, "Certificate Exception");
    } catch (IOException e) {
        Log.w(TAG, "IO Exception", e);
    }
    return null;
}
