  byte[] wholeFileByte = null;
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, DownloadBookAsyncTask.ivspec);
    File file = new File(context.getFilesDir().getParentFile().getPath() + "/download/" + id + "/xmldata.xml");
    FileInputStream fis = new FileInputStream(file);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    byte data[] = new byte[4096];
    int count;
    while ((count = cis.read(data)) != -1) {
        bos.write(data, 0, count);
    }

    if(cis != null)
        cis.close();
    if(bos != null)
        bos.close();
    if(fis != null)
        fis.close();

    wholeFileByte = bos.toByteArray();
    String kk = new String(wholeFileByte, "UTF-8");
