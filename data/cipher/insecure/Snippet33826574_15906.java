private SecretKeySpec secretKey;
public void setKey() {
    skey = "mykey";
    MessageDigest sha = null;
    try {
        key = skey.getBytes("UTF-8");
        logger.debug("Key length ====> " + key.length);
        sha = MessageDigest.getInstance("SHA-512");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        secretKey = new SecretKeySpec(key, "AES");

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}
public String decrypt(String strToDecrypt) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

        cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
        setDecryptedString(new String(cipher.doFinal(Base64
                .decodeBase64(strToDecrypt))));

    } catch (Exception e) {
        System.out.println("Error while decrypting: " + e.toString());
    }
    return null;
}
