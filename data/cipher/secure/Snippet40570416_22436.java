public String RSAEncryptString(String strInputString, int dwKeySize, PublicKey p) {
    StringBuilder stringBuilder = null;
    try {
        int keySize = dwKeySize / 8;
        byte[] bytes = strInputString.getBytes("UTF-32LE");
        int maxLength = keySize - 42;
        int dataLength = bytes.length;
        int iterations = dataLength / maxLength;
        stringBuilder = new StringBuilder();
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");//4.4
        cipher.init(Cipher.ENCRYPT_MODE, p);
        //cipher.init(Cipher.DECRYPT_MODE, p, new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        for (int i = 0; i <= iterations; i++) {
            int index = (dataLength - maxLength * i > maxLength) ? maxLength : dataLength - maxLength * i;
            int offset = maxLength * i;
            byte[] tempBytes = new byte[index];
            System.arraycopy(bytes, offset, tempBytes, 0, tempBytes.length);
            byte[] encryptedBytes = cipher.doFinal(tempBytes);
            byte[] encryptedBytes1 = onReverse(encryptedBytes);
            stringBuilder.append(Base64.encodeToString(encryptedBytes1, Base64.NO_WRAP));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return stringBuilder.toString();
}
