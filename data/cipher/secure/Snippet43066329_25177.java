public void encrypt(String input, String output) {
    File inputFile;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    Cipher cipher;
    byte[] inputBytes;
    byte[] outputBytes;

    try {
        System.out.println("encrypt");

        cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        inputFile = new File(input);
        inputStream = new FileInputStream(inputFile);
        outputStream = new FileOutputStream(output);
        inputBytes = new byte[(int) input.length()];
        int readBytes = inputStream.read(inputBytes);
        outputBytes = cipher.doFinal(inputBytes, 0, readBytes);
        outputStream.write(outputBytes);

        System.out.println("fin encrypt");

        inputStream.close();
        outputStream.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void decrypt(String input, String output) {
    File inputFile;
    FileInputStream inputStream;
    FileOutputStream outputStream;
    Cipher cipher;
    byte[] inputBytes;
    byte[] outputBytes;

    try {
        System.out.println("decrypt");

        cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privKey);

        inputFile = new File(input);
        inputStream = new FileInputStream(inputFile);
        outputStream = new FileOutputStream(output);
        inputBytes = new byte[(int) inputFile.length()];
        int readBytes = inputStream.read(inputBytes);
        outputBytes = cipher.doFinal(inputBytes, 0, readBytes);
        outputStream.write(outputBytes);

        System.out.println("Decryption result: " + new String(outputBytes, "UTF-8"));
        System.out.println("fin decrypt");

        inputStream.close();
        outputStream.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
