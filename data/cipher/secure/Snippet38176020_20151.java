private Cipher getCipher() {
    try {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
            return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL"); // error in android 6: InvalidKeyException: Need RSA private or public key
        }
        else { // android m and above
            return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround"); // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
        }
    } catch(Exception exception) {
        throw new RuntimeException("getCipher: Failed to get an instance of Cipher", exception);
    }
}
