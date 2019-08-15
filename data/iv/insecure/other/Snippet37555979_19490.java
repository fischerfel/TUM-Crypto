public static String decrypt(String pValor) throws UnsupportedEncodingException {

    byte vBytesDecodificados[] = null;

    try {

        KeySpec vClave = new DESKeySpec("MyKey".getBytes("UTF-8"));
        SecretKey vClaveSecreta = SecretKeyFactory.getInstance("DES").generateSecret(vClave);

        IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("1234567890ABCDEF".toCharArray()));

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, vClaveSecreta, iv);

        vBytesDecodificados = cipher.doFinal(Base64.decodeBase64(pValor.getBytes()));

    } catch (Exception e) {

    }

    return new String(vBytesDecodificados, "UTF-8");
}

public static String encrypt(String pValor) throws UnsupportedEncodingException {

    byte vBytesCodificados[] = null;

    try {

        KeySpec vClave = new DESKeySpec("MyKey".getBytes("UTF-8"));
        SecretKey vClaveSecreta = SecretKeyFactory.getInstance("DES").generateSecret(vClave);

        IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("1234567890ABCDEF".toCharArray()));

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, vClaveSecreta, iv);

        byte[] utf8 = pValor.getBytes("UTF8");
        byte[] enc = cipher.doFinal(utf8);
        vBytesCodificados = Base64.encodeBase64(enc);

    } catch (Exception e) {

    }

    return new String(vBytesCodificados, "UTF-8");
}
