public String getJSON() {
            String jsonString = "";

    try {
            String algorithm = "DESede";
            jsonString = "";
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            DESedeKeySpec kspec = new DESedeKeySpec(readKey().getBytes());
            SecretKey key = skf.generateSecret(kspec);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherInputStream cip = new CipherInputStream(context.openFileInput("filename"), cipher);

            // here I have to get the result of decoding and write it to the jsonString

            cip.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }
