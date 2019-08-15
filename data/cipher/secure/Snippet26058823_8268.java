public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
        throws Exception {

    byte[] decrypted = null;
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(ivandcipher.get(0)));
    decrypted = cipher.doFinal(fileData);
    return decrypted;
}
