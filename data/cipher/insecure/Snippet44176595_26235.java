 public string encrypt(String input, string key) {
    SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skey);
    crypted = cipher.doFinal(input.getBytes("UTF-8"));
    return Base64.encodeToString(crypted,Base64.NO_WRAP);
}
