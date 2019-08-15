private static Key generateKey() throws Exception {

    Key key = new SecretKeySpec(Files.readAllBytes(Paths.get("D:/Temp/cr.key")), "AES");

    return key;
}

public static byte[] decrypt(byte[] encryptedData) throws Exception {
    Key key = generateKey();

    Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

    IvParameterSpec ivSpec = new IvParameterSpec(key.getEncoded());
    c.init(Cipher.DECRYPT_MODE, key, ivSpec);

    System.out.println(c.getBlockSize());


    c.update(encryptedData);
    byte[] decValue = c.doFinal();
    return decValue;
}

public static void main(String[] args) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    byte[] bb = decrypt(Files.readAllBytes(Paths.get("d:\\Temp\\cr~\\OEBPS\\Chapter001.html")));
           //decompressFile(bb, new File("D:\\Temp\\enc.html"));
}
