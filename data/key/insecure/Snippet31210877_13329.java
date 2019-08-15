public static final String KEY = "thisismysupersecretkeyglhf1";


public static String encrypt(String toDecrypt) throws NoSuchPaddingException,
        NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DecoderException, UnsupportedEncodingException {

    Cipher cipher = Cipher.getInstance("CAST6/ECB/NoPadding");

    SecretKeySpec key=new SecretKeySpec(KEY.getBytes(),"CAST6");

    cipher.init(Cipher.ENCRYPT_MODE, key);

    String decoded=org.apache.commons.codec.binary.Base64.encodeBase64String(cipher.doFinal(toDecrypt.getBytes()));

    return decoded;
}

public static String decrypt(String toDecrypt) throws NoSuchPaddingException,
        NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DecoderException, UnsupportedEncodingException {

    Cipher cipher = Cipher.getInstance("CAST6/ECB/NoPadding");

    SecretKeySpec key=new SecretKeySpec(KEY.getBytes(),"CAST6");

    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] decoded=org.apache.commons.codec.binary.Base64.decodeBase64(toDecrypt);

    return new String(cipher.doFinal(decoded));
}
