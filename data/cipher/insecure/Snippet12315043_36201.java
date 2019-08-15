KeyGenerator keygen = KeyGenerator.getInstance("DES");
        SecretKey key = keygen.generateKey(); //generate key
        //encrypt file here first
        byte[] plainData;
        byte[] encryptedData;
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        FileInputStream in = new FileInputStream(mFile); //obtains input bytes from a file
        plainData = new byte[(int)mFile.length()]; 
        in.read(plainData); //Read bytes of data into an array of bytes
        encryptedData = cipher.doFinal(plainData); //encrypt data               
        ByteArrayInputStream fis = new ByteArrayInputStream(encryptedData);
        //save encrypted file to dropbox

        // By creating a request, we get a handle to the putFile operation,
        // so we can cancel it later if we want to
      // FileInputStream fis = new FileInputStream(mFile);
        String path = mPath + mFile.getName();
        mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(),
                new ProgressListener() {
