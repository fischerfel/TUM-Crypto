      public String decryptString(String alias, String decrypted) {
    try {

        KeyStore.Entry entry;
        //ERROR HAPPENS HERE.
        entry = keyStore.getEntry(alias, null);

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;

        Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());


        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(Base64.decode(decrypted, Base64.DEFAULT)), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i);
        }

        String finalText = new String(bytes, 0, bytes.length, "UTF-8");
        return finalText;

    } catch (IOException | KeyStoreException | NoSuchPaddingException | UnrecoverableEntryException | InvalidKeyException | NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "Error";
}
