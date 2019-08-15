public Crypto() {
    try {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        ecipher = Cipher.getInstance("PBEWithMD5AndDES");
        dcipher = Cipher.getInstance("PBEWithMD5AndDES");
        byte[] salt = new byte[8];
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (Exception e) {
    }
}

@SuppressWarnings("resource")
public static List<String> decrypt(String file) {
    List<String> list = new LinkedList<String>();
    try {
        InputStream in = new CipherInputStream(new FileInputStream(file), dcipher);
        int numRead = 0;
        while ((numRead = in.read(buffer)) >= 0) {
            list.add(new String(buffer, 0, numRead);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return list;
}
