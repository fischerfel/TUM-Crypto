    //Work out MD5 of the HashMap result (convert it to bytes with objectOutputStream, and MD5 the bytes)
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
    ObjectOutputStream out = new ObjectOutputStream(bos) ;
    out.writeObject(result);
    out.close();
    byte[] md5 = messageDigest.digest(bos.toByteArray();

    //Encrypt the httpURLConnection response stream, and send the HashMap result and the md5 over the stream
    Cipher symmetricCipher = Cipher.getInstance("DES");
    symmetricCipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
    CipherOutputStream cipherOutput = new CipherOutputStream(response.getOutputStream(), symmetricCipher);
    BufferedOutputStream bufferedOutput = new BufferedOutputStream(cipherOutput);
    ObjectOutputStream objectOutput = new ObjectOutputStream(out);
    objectOutput.writeObject(result);
    objectOutput.writeObject(md5);
    objectOutput.flush();
