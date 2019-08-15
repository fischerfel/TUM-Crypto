 @SafeVarargs
@Override
protected final File doInBackground(HashMap<String, Object>... params) {
    String path = (String) params[0].get(FILE_PATH);
    String fileName = String.valueOf(params[0].get(FILE_NAME));
    boolean encrypted = (boolean) params[0].get(ENCRYPTED);

    File root = android.os.Environment.getExternalStorageDirectory();
    File dir = new File(root.getAbsolutePath() + File.separator + path + File.separator);
    File file;

    if (!encrypted) {
        file = new File(dir + File.separator + fileName);
        return file;
    }

    file = new File(dir + File.separator + Cypher.md5(fileName));
    File tempMp3 = null;
    try {
        tempMp3 = File.createTempFile(TEMP, MP3, context.getCacheDir());
        tempMp3.deleteOnExit();
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        FileInputStream fis = new FileInputStream(file);
        byte[] key = (DownloadManager.salt + DownloadManager.cryptPassword).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 8);
        SecretKeySpec sks = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        FileOutputStream fos = new FileOutputStream(tempMp3);

        int b;
        byte[] d = new byte[80192];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }

        fos.flush();
        fos.close();
        cis.close();

    } catch (IOException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }

    return tempMp3;
}
