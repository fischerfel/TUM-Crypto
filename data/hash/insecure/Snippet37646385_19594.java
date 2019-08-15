public static void main(String[] args) {
    String data = getData();
    String key = generateKey();
    byte[] encrypted = encryptData(data, key);
    String str_encrypted = new String(encrypted);
    String url = pushToServer(str_encrypted);
}

private static byte[] encryptData(String data, String key) {

    try {
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(key.getBytes("UTF-8"));
        byte[] digest = md.digest();

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(digest, "AES");
        c.init(Cipher.ENCRYPT_MODE, k);

        byte[] tobeencrypted = data.getBytes("UTF-8");

        return c.doFinal(tobeencrypted);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;

}
