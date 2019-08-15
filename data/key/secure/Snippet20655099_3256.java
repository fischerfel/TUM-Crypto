public String decryptXml() {
    String data = null;
    File file = new File(context.getFilesDir().getParentFile().getPath() + "/download/" + id + "/xmldata.xml");
    int size = (int) file.length();
    byte[] bytes = new byte[size];
 try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(context).getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, DownloadBookAsyncTask.spec);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(bytes, 0, bytes.length);
        bis.close();
        byte[] decrypted = cipher.doFinal(bytes);
    }
