public void encryptFile(String inputFileName, String outputFileName, String keyFileName) throws Exception {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keygen.init(random);

            SecretKey key = keygen.generateKey();

            //Packages with the RSA public key
            System.out.println("keyFileName : " + keyFileName);
            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(keyFileName));
            Key publicKey = (Key) keyIn.readObject();
            keyIn.close();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, publicKey);

            byte[] wrappedKey = cipher.wrap(key);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFileName));
            out.writeInt(wrappedKey.length);
            out.write(wrappedKey);

            InputStream in = new FileInputStream(inputFileName);
            cipher = cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            crypt(in, out, cipher);
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("++++ Exception encryptFile ++++");
            e.printStackTrace();
            throw e;
        }
}

private void crypt(InputStream in, OutputStream out, Cipher cipher)
            throws IOException, GeneralSecurityException {

        System.out.println("in inside crypt ---- " + in);
        System.out.println("in inside crypt ---- " + out);
        System.out.println("cipher inside crypt  ----- " + cipher);

        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean done = false;

        while (!done) {
            inLength = in.read(inBytes);

            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else {
                done = true;
            }
        }


        if (inLength > 0) {
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        } else {
            outBytes = cipher.doFinal();
        }

        out.write(outBytes);
}
