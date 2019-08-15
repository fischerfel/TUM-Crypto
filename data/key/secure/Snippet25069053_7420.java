static void setKey(byte[] keybytes, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchProviderException
{
    /**
     * crypto is specifically stated here because without using AndroidOpenSSL for the SHA1PRNG breaks on some phones,
     * PRNGFixes.apply() should be called if using this
     * https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html 
     */
    random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
    key = new SecretKeySpec(keybytes, "AES");
    ivspec = new IvParameterSpec(iv);
    encryptcipher = Cipher.getInstance("AES/CFB/NoPadding", "SC");
    encryptcipher.init(Cipher.ENCRYPT_MODE, key, ivspec, random);

    decryptcipher = Cipher.getInstance("AES/CFB/NoPadding", "SC");
    decryptcipher.init(Cipher.DECRYPT_MODE, key, ivspec, random);       
}
