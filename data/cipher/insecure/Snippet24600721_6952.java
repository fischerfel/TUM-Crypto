private static Serializable loadFromFile(String fileName, Context ctx) {
    Cipher cipher = null;
    Serializable userList = null;
    try {
        cipher = Cipher.getInstance("DES");

        // Code to write your object to file
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        CipherInputStream cipherInputStream = null;

        FileInputStream fos = ctx.openFileInput(fileName);
        cipherInputStream = new CipherInputStream(new BufferedInputStream(
                fos), cipher);

        ObjectInputStream inputStream = null;
        inputStream = new ObjectInputStream(cipherInputStream);
        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        SealedObject sealedObject = null;
        sealedObject = (SealedObject) inputStream.readObject();
        userList = (Serializable) sealedObject.getObject(cipher);
        inputStream.close();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (StreamCorruptedException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (IOException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (BadPaddingException e) {
        e.printStackTrace();
        return e.getMessage();
    }
    return userList;
}
