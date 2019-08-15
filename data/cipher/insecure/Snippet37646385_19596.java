private static String decrypt(String data, String key){
    try {


    MessageDigest md = MessageDigest.getInstance("MD5");

    md.update(key.getBytes("UTF-8"));
    byte[] digest = md.digest();

    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(digest, "AES");
    c.init(Cipher.DECRYPT_MODE, k);

    byte[] decrypt = c.doFinal(data.getBytes("UTF-8"));

    String decrypted = new String(decrypt);

    return decrypted;
    } catch (Exception e){
        e.printStackTrace();
        return "Something went wrong";
    }
}
