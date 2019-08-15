public static void encrypt(Serializable object, OutputStream ostream, byte[] keyy, String transformationnn) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        try {
            // Length is 16 byte
            SecretKeySpec sks = new SecretKeySpec(keyy, transformationnn);

            // Create cipher
            Cipher cipher = Cipher.getInstance(transformationnn);
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            SealedObject sealedObject = new SealedObject(object, cipher);

            // Wrap the output stream
            CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
            ObjectOutputStream outputStream = new ObjectOutputStream(cos);
            outputStream.writeObject(sealedObject);
            outputStream.close();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public static Object decrypt(InputStream istream, byte[] keyy, String transformationnn) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(keyy, transformationnn);
        Cipher cipher = Cipher.getInstance(transformationnn);
        cipher.init(Cipher.DECRYPT_MODE, sks);

        CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
        ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
        SealedObject sealedObject;
        try {
            sealedObject = (SealedObject) inputStream.readObject();
            return sealedObject.getObject(cipher);
        } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
