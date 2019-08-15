 public void decryptString(String alias) 
 {
     try
    {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
        RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

        Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        output.init(Cipher.DECRYPT_MODE, privateKey);

        String cipherText = encryptedText.getText().toString();
        CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
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
        decryptedText.setText(finalText);

    } 
    catch (Exception e) {
        Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
        Log.e(TAG, Log.getStackTraceString(e));
    }
}
