public static void main(String[] args) {

        String codedtext = null;
        try {
            codedtext = readFile("ect.txt", StandardCharsets.UTF_8);                
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String decodedtext = null;
        try {
            decodedtext = _decrypt(codedtext,"abcdefgh");
        } catch (Exception e) {
            e.printStackTrace();
        }

}

private static String readFile(String path, Charset encoding) 
          throws IOException 
        {
          byte[] encoded = Files.readAllBytes(Paths.get(path));
          return new String(encoded, encoding);
        }

private static String _encrypt(String message, String secretKey) throws Exception {

    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
    byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

    SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] plainTextBytes = message.getBytes("utf-8");
    byte[] buf = cipher.doFinal(plainTextBytes);
    byte [] base64Bytes = Base64.encodeBase64(buf);
    String base64EncryptedString = new String(base64Bytes);

    return base64EncryptedString;
}
