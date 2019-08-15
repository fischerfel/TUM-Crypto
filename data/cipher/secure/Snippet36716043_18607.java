public String getEncryptedString(String s) throws Exception {

    try {

        kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        skey = kgen.generateKey();

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return getString(cipher.doFinal(getBytes(s)));
}
