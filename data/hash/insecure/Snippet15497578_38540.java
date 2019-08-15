BASE64Decoder decoder = new BASE64Decoder();
BASE64Encoder encoder = new BASE64Encoder();

public String encryptValueWithBlowfish(String data, String secretKey) {

    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    try {
        SecretKeySpec key = new SecretKeySpec(decoder.decodeBuffer(secretKey), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/NoPadding", "BC");
        String iv = "\0\0\0\0\0\0\0\0";
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, key, ivs);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        return encoder.encode(sha.digest(cipher.doFinal(decoder.decodeBuffer(data))));
    } catch (Exception e) {
        lg.info("Failed to encryptValueWithBlowfish: " + e.getMessage());
        return "";
    }
}
