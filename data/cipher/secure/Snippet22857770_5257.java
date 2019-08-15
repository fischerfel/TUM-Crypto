public static String encrypt(String msg, String fileName) throws Exception {
    try {
        Cipher cipher = Cipher.getInstance("RSA");
        RSAPublicKey cryptPublicKey = readPublicKeyFromFile(fileName);
        System.out.println("public "+cryptPublicKey);
        cipher.init(1, cryptPublicKey);
        String tempStr = msg;
        int modulus = tempStr.length() / 8;
        if (modulus != 0) {
            for (int i = modulus; i < 8; i++)
                tempStr = tempStr + " ";
        }
        byte plainData[] = tempStr.getBytes();
        byte binaryCryptData[] = cipher.doFinal(plainData);
        BASE64Encoder encoder = new BASE64Encoder();
        String encData = encoder.encode(binaryCryptData);
        msg = encData;
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
    return msg;
}

private static RSAPublicKey readPublicKeyFromFile(String fileName) throws Exception {
    RSAPublicKey cryptPublicKey = null;
    try {
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        FileInputStream fis = new FileInputStream(fileName);
        byte cryptKey[] = new byte[fis.available()];
        fis.read(cryptKey);
        fis.close();
        cryptKey = (new BASE64Decoder()).decodeBuffer(new String(cryptKey));
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(cryptKey);
        cryptPublicKey = (RSAPublicKey) keyFac.translateKey(keyFac.generatePublic(encodedKeySpec));
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
    return cryptPublicKey;
}
