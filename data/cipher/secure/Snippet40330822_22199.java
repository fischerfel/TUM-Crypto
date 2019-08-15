public static String decryptString(String alias,String cipherText) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
           // RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

        Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        output.init(Cipher.DECRYPT_MODE,  privateKeyEntry.getPrivateKey());

        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte)nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }

        String finalText = new String(bytes, 0, bytes.length, "UTF-8");
        return finalText;
        //decryptedText.setText(finalText);

    } catch (Exception e) {
        Toast.makeText(context, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
        Log.e("DecryptStringTAG", Log.getStackTraceString(e));
    }
    return "EMPTY";
}
