static String decrypt(String strInput) throws IOException, NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream(strInput);

    int endFile = strInput.length() - 4;
    String strOut = strInput.substring(0, endFile) + "xx.jpg"; 

    FileOutputStream fos = new FileOutputStream(strOut);

    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
              "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];

    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
    return strOut;
}
