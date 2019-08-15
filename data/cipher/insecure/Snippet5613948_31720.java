    //Decrypt the httpURLConnection response stream
    Cipher symmetricCipher = Cipher.getInstance("DES");
    symmetricCipher.init(Cipher.DECRYPT_MODE, symmetricKey);
    CipherInputStream cipherInput = new CipherInputStream(httpInput, symmetricCipher);
    BufferedInputStream bufferedInput = new BufferedInputStream(cipherInput);           

    //read HashMap and MD5
    ObjectInputStream objectInput = new ObjectInputStream(in);
    HashMap<String, Object> result = (HashMap<String, Object>) objectInput.readObject();
    byte[] hash1 = (byte[]) objectInput.readObject();

    //workout hash of the Hashmap received.
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
    ObjectOutputStream out = new ObjectOutputStream(bos) ;
    out.writeObject(result);
    out.close();
    byte[] hash2 = messageDigest.digest(bos.toByteArray();

    // Compare two hashes
    if (!Arrays.equals(hash1, hash2)) {
        System.out.println("Result received does not match hash, stopping list operation");
        return;
    }
