public static String encryptString(String password, String source, String fileName, String fileDir) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, FileNotFoundException, IOException {
    FileOutputStream fos = null;
    CipherInputStream cis;

    byte key[] = password.getBytes();
    SecretKeySpec secretKey = new SecretKeySpec(key, "DES");

    Cipher encrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
    encrypt.init(Cipher.ENCRYPT_MODE, secretKey);

    InputStream fileInputStream = new ByteArrayInputStream(source.getBytes());//Here I am getting file data as byte array. You can convert your file data to InputStream  by other way too.

    File dataFile = new File(fileDir, fileName); //dataDir is location where my file is stored
    if (!dataFile.exists()) {
        cis = new CipherInputStream(fileInputStream, encrypt);
        try {
            fos = new FileOutputStream(dataFile);
            byte[] b = new byte[32];
            int i;
            while ((i = cis.read(b)) != -1) {
                fos.write(b, 0, i);
            }
            return fileName;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                cis.close();
                fileInputStream.close();
            } catch (IOException e) {
                //IOException
            }
        }
    }
    return "";
}
