public static byte[] encrypt(byte encrypt[], byte en_key[]) {

    if(encrypt.length % 8 != 0){ //not a multiple of 8
        //create a new array with a size which is a multiple of 8
        byte[] padded = new byte[encrypt.length + 8 - (encrypt.length % 8)];

        //copy the old array into it
        System.arraycopy(encrypt, 0, padded, 0, encrypt.length);
        encrypt = padded;
    }

    try {
        SecretKeySpec key = new SecretKeySpec(en_key, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(encrypt);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
