 public void executeAsyncDownload(String urlFile, String id, int position, HandlerCallback callback) {

    String encryptedName = Cypher.md5(id);

    if (MediaUtils.containsFile(encryptedName)) {
        callback.onDownloadFinish(position);
        return;
    }

    File dir = MediaUtils.getDestinationFolder(destination);

    if (!dir.exists()) {
        dir.mkdir();
    }

    try {
        if (canceled)
            return;

        callback.onDownloadStart(position);
        URL url = new URL(urlFile);
        URLConnection connection = url.openConnection();
        connection.connect();
        int tamFile = connection.getContentLength();
        String filePath = MediaUtils.getFilePath(MediaUtils.tempPath + encryptedName).toString();

        InputStream fis = new BufferedInputStream(url.openStream());
        OutputStream fos = new FileOutputStream(filePath);
        File file = new File(filePath);

        byte data[] = new byte[80192];
        int count;
        long total = 0;

        while ((count = fis.read(data)) != -1) {
            total += count;
            if (tamFile > 0) {
                int percentage = (int) (total * 100 / tamFile);
                if (percentage % 20 == 0)
                    callback.onDownloadProgress(percentage, position);
            }

            fos.write(data, 0, count);

            if (canceled) {
                MediaUtils.deleteFile(file);
                return;
            }
        }


        if (canceled)
            return;


        byte[] key = (salt + cryptPassword).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 8);
        SecretKeySpec sks = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        int b;
        byte[] d = new byte[8192];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }

        OutputStream outputEncrypted = new FileOutputStream(dir + File.separator + encryptedName);
        outputEncrypted.write(d);
        outputEncrypted.close();


        fos.flush();
        fos.close();
        fis.close();

        MediaUtils.deleteFile(file);//delete temp file

        callback.onDownloadFinish(position);
    } catch (Exception e) {
        e.printStackTrace();
        callback.onDownloadError(position);
    }
}
