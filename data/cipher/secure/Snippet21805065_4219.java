 public static Serializable AESObjectDedcoder(String password, String path) {
        Cipher cipher = null;
        Serializable userList = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS7Pdding");

        //Code to write your object to file
        cipher.init(Cipher.DECRYPT_MODE, fromStringToAESkey(password));         
        CipherInputStream cipherInputStream = null;
        cipherInputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(path)), cipher);

        ObjectInputStream inputStream = null;
        inputStream = new ObjectInputStream(cipherInputStream);
        SealedObject sealedObject = null;
        sealedObject = (SealedObject) inputStream.readObject();
        userList = (Serializable) sealedObject.getObject(ciper);  
        return userList;
    }
