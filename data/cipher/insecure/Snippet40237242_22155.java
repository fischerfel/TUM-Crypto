protected String decrypt(String value){
    try {
        final byte[] bytes = value!=null ? Base64.decode(value,Base64.DEFAULT) : new byte[0];
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec( Secure.getString(context.getContentResolver(), Secure.ANDROID_ID).getBytes(UTF8), 20));
        return new String(pbeCipher.doFinal(bytes),UTF8);

    } catch( Exception e) {
        throw new RuntimeException(e);
    }
}
