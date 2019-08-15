public void createDecryptedFile(File decryptedFileDir, File decryptedFile,
                                File encryptedFile) {
    try {
        if (!decryptedFileDir.exists()) {
            decryptedFileDir.mkdirs();
        }
        Cipher decipher;
        decryptedFile.createNewFile();
        deleteFile = decryptedFile;
                        FileInputStream encryptedFileInputstream = new FileInputStream(
                encryptedFile);
        FileOutputStream decryptedFileOutputstream = new FileOutputStream(
                decryptedFile);

        decipher = Cipher.getInstance("AES");
        Key key = generateKey();
        decipher.init(Cipher.DECRYPT_MODE, key);

        CipherOutputStream cos = new CipherOutputStream(
                decryptedFileOutputstream, decipher);

        byte data[] = new byte[10000 * 1024];

        int count;
        try {

            while ((count = encryptedFileInputstream.read(data)) != -1  && !stopConversion) {
                Log.d("#########", "##########");

                total += count;
                Log.e("convert count", total + "");

                cos.write(data, 0, count);

                final long l = encryptedFile.length();

                runOnUiThread(new Runnable() {
                    public void run() {

                        // Show percentage 
                        loadingpercent.setText("" + (int) (total * 100 / l) + "%");
                    }
                });

                Log.d("$$$$$$$$",""+encryptedFileInputstream.read(data));

            }
