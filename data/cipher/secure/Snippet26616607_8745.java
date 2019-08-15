
    public static void generateFileEncrypt(File fileInput, PrivateKey privateKey, String folderSave) throws Exception {
        String fileOutput = folderSave + "\" + fileInput.getName() + ENCRYPTED_FILENAME_SUFFIX;
        DataOutputStream output = new DataOutputStream(new FileOutputStream(fileOutput));
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, privateKey);
        KeyGenerator rijndaelKeyGenerator = KeyGenerator.getInstance("Rijndael");
        rijndaelKeyGenerator.init(128);
        Key rijndaelKey = rijndaelKeyGenerator.generateKey();
        byte[] encodedKeyBytes = rsaCipher.doFinal(rijndaelKey.getEncoded());
        output.writeInt(encodedKeyBytes.length);
        output.write(encodedKeyBytes);
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        output.write(iv);
        IvParameterSpec spec = new IvParameterSpec(iv);
        Cipher symmetricCipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
        symmetricCipher.init(Cipher.ENCRYPT_MODE, rijndaelKey, spec);
        try (
                CipherOutputStream cos = new CipherOutputStream(output, symmetricCipher);
                FileInputStream fis = new FileInputStream(fileInput)) {
            int theByte;
            byte[] data = new byte[16];
            while ((theByte = fis.read(data)) != -1) {
                System.out.print(new String(data));
                cos.write(data, 0, theByte);
            }
            System.out.println("\n\n");
            cos.flush();
        }
    }
