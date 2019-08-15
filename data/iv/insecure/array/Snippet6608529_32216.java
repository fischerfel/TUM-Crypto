private static void decryptFile(String filename) throws Exception {
    FileInputStream fis = null;
    BufferedReader br = new BufferedReader(new FileReader(filename));

    File file = new File(filename + "-decrypted");
    file.createNewFile();
    Writer out = new OutputStreamWriter(new FileOutputStream(filename + "-decrypted"), "UTF-8");

    String line = null;
    try {
         while (( line = br.readLine()) != null){
             line = decrypt(Base64.decodeBase64(line));
             out.write(line);
         }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (br != null) {
            br.close();
        }
        if (fis != null) {
            fis.close();
        }
        if (out != null) {
            out.close();
        }
    }
}

public static String decrypt(byte[] line) throws Exception {
    byte[] keyBytes = "1234123412341234".getBytes();
    final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

    final SecretKey secretkey = new SecretKeySpec(keyBytes, "AES");
    final IvParameterSpec IV = new IvParameterSpec(ivBytes);
    final Cipher decipher = Cipher.getInstance("AES/CFB8/NoPadding");
    decipher.init(Cipher.DECRYPT_MODE, secretkey, IV);
    final byte[] plainText = decipher.doFinal(line);

    return new String(plainText, "UTF-8").trim();
}
