public static String decrypt(String encrypted, KeyPair keys) {
    Cipher dec;
    try {

        dec = Cipher.getInstance("RSA/NONE/NoPadding"); //Exception raised
        dec.init(Cipher.DECRYPT_MODE, keys.getPrivate());

    } catch (GeneralSecurityException e) {
        throw new RuntimeException("RSA algorithm  not supported", e);//Catch block executed
    }
}
