
    public File decryptFile(File fileInput, X509Certificate certificate) throws BadPaddingException, Exception {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileInput))) {
            byte[] encryptedKeyBytes = new byte[dis.readInt()];
            dis.readFully(encryptedKeyBytes);
            PublicKey publicKey = certificate.getPublicKey();
            rsaCipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] rijndaelKeyBytes = rsaCipher.doFinal(encryptedKeyBytes);
            SecretKey rijndaelKey = new SecretKeySpec(rijndaelKeyBytes, "Rijndael");
            byte[] iv = new byte[16];
            dis.read(iv);
            IvParameterSpec spec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("Rijndael/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, rijndaelKey, spec);
            try (CipherInputStream cis = new CipherInputStream(dis, cipher)) {
                try (FileOutputStream fos = new FileOutputStream(fileInput.getAbsolutePath() + ".xml")) {
                    byte[] data = new byte[16];
                    int theByte;
                    while ((theByte = cis.read(data)) != -1) {
                        System.out.print(new String(data));
                        fos.write(data, 0, theByte);
                    }
                    System.out.println("\n\n");
                }
            }
        }
        return new File(fileInput.getAbsolutePath() + ".xml");
    }
