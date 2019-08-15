public void getDataFromRSA(String sendname, PrivateKey privateKey) {
    try {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(sendname)));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int length = 0;
        int allLength = 0;
        byte[] buffer = new byte[128];
        byte[] bufferAC = null;
        byte[] outData = null;
        byte[] allData = null;
        byte[] tmpData = null;
        while ( (length = bis.read(buffer)) != -1) {
            if (length < 128) {
                bufferAC = new byte[length];
                System.arraycopy(buffer, 0, bufferAC, 0, length);
                outData = cipher.doFinal(bufferAC);
            } else {
                outData = cipher.doFinal(buffer); // HERE IS THE ERROR
            }
            allLength += outData.length;
            tmpData = allData;
            allData = new byte[allLength];
            System.arraycopy(tmpData, 0, allData, 0, tmpData.length);
            System.arraycopy(outData, 0, allData, tmpData.length, outData.length);
        }
    } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | ClassNotFoundException | InvalidKeySpecException e) {
        e.printStackTrace();
    }
}
