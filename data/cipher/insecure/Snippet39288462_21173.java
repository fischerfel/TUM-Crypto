public void encrypt(String desKey, String zipFileName, String fileName) {
    InputStream inputStream = null;
    FileInputStream fileInputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
        fileInputStream = new FileInputStream(zipFileName);

        SecretKey keySpec = new SecretKeySpec(desKey.getBytes(), "DESede");

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        InputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);

        ZipInputStream zipInputStream = new ZipInputStream(cipherInputStream);
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        inputStream = zipInputStream;
        if (nextEntry == null) {
            System.out.println("error");
            inputStream = null;
        }
        fileOutputStream = new FileOutputStream(fileName);
        IOUtils.copy(inputStream, fileOutputStream);
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if (inputStream != null) {
                inputStream.close();
            } else if (fileInputStream != null) {
                fileInputStream.close();
            } else if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
