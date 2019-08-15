    //httpOutput is the HTTPUrlConnection request stream
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipher.init(Cipher.ENCRYPT_MODE, certificate);
    CipherOutputStream cipherOutput = new CipherOutputStream(httpOutput, rsaCipher);

    BufferedOutputStream bufferedOutput = new BufferedOutputStream(cipherOutput);
    ObjectOutputStream objectOutput = new ObjectOutputStream(bufferedOutput );
