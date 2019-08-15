static String decrypt(String strInput) throws IOException,
    NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    FileInputStream fis = new FileInputStream(strInput);

    int endFile = strInput.length() - 4;
    String strOut = strInput.substring(0, endFile) + "xx.txt"; 

    FileOutputStream fos = new FileOutputStream(strOut);

    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);

    int b;
    byte[] d = new byte[8];
    while ((b = fis.read(d)) != -1) {
        fos.write(cipher.update(d));
    }
    fos.write(cipher.doFinal());

    fos.flush();
    fos.close();
    fis.close();
    return strOut;
}
