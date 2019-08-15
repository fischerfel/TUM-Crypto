private static String decrypt(String seed, String encrypted) throws Exception {
    byte[] keyb = seed.getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(keyb);
    SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
    Cipher dcipher = Cipher.getInstance("AES");
    dcipher.init(Cipher.DECRYPT_MODE, skey);

    byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
    return new String(clearbyte);
}

private static byte[] toByte(String hexString) {
    int len = hexString.length()/2;
    byte[] result = new byte[len];
    for (int i = 0; i < len; i++) {
        result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
    }
    return result;
}
