private static void decryptToFile(String string, String string2)
        throws Exception {

    try {
        File encryptedFile = new File("encrypted.txt");

        byte[] encrypted = getContents(encryptedFile).getBytes();

        cip = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cip.init(Cipher.DECRYPT_MODE, pubKey);

        byte[] cipherData = cip.doFinal(encrypted);

        String decryptedData = cipherData.toString();
        BufferedWriter out = new BufferedWriter(new FileWriter(
                "decrypted.txt"));
        out.write(decryptedData);
        out.close();

    } catch (Exception e) {
        throw e;
    }
}
