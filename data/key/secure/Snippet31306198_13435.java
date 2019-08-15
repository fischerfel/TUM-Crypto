private static byte[] filesize = new byte[8];
private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

public static void decrypt(String inputFile, String outputFile, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    FileInputStream fis = new FileInputStream(inputFile);
    FileOutputStream fos = new FileOutputStream(outputFile);
    fis.read(filesize, 0, 8);
    System.out.println(new String(filesize));
    fis.mark(9);
    fis.read(ivBytes, 0, 16);
    System.out.println(new String(ivBytes));
    fis.mark(25);
    IvParameterSpec iv = new IvParameterSpec(ivBytes);
    SecretKeySpec sks = new SecretKeySpec(password.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, sks, iv);
    File file = new File(inputFile);
    int byteLength = (int) file.length() - 24;
    System.out.println(Integer.toString(byteLength));
    byte[] bytes = new byte[byteLength];
    byteLength = fis.read(bytes);
    System.out.println(Integer.toString(byteLength));
    System.out.println(new String(bytes));
    InputStream bytesStream = new ByteArrayInputStream(bytes);
    CipherInputStream cis = new CipherInputStream(bytesStream, cipher);

    int b;
    byte[] d = new byte[1024];
    while((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
}
