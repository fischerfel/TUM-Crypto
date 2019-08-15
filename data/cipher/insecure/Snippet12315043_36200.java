        // Now pick a random one
        int index = (int)(Math.random() * thumbs.size());
        Entry ent = thumbs.get(index);
        String path = ent.path;
        mFileLen = ent.bytes;

        String cachePath = mContext.getCacheDir().getAbsolutePath() + "/" + IMAGE_FILE_NAME;
        try {

            KeyGenerator keygen = KeyGenerator.getInstance("DES");
            SecretKey key = keygen.generateKey(); //generate key
            byte[] encryptedData;
            byte[] decryptedData;
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            //File f = new File(encryptfilepath);
            FileInputStream in = new FileInputStream(cachePath);
            encryptedData = new byte[(int)cachePath.length()];
            in.read(encryptedData);
            decryptedData = cipher.doFinal(encryptedData);
            ByteArrayOutputStream fis = new ByteArrayOutputStream(decryptedData);
            //ByteArrayInputStream fis = new ByteArrayInputStream(decryptedData);
             mFos = new FileOutputStream(new File(fis));

          //  mFos = new FileOutputStream(cachePath);
        } catch (FileNotFoundException e) {
            mErrorMsg = "Couldn't create a local file to store the image";
