 public static String decryptPrivateKey(String privateKeyFilename) throws Exception {
    String password = getPassword();

    String fileExtension = privateKeyFilename.substring(privateKeyFilename.lastIndexOf("."), privateKeyFilename.length());
    String decryptedFilename = privateKeyFilename.replace(fileExtension, "") + " decrypted" + fileExtension;

    FileOutputStream output = new FileOutputStream(decryptedFilename);
    FileInputStream privateKeyInput = new FileInputStream(privateKeyFilename);

    byte[] nonce = new byte[12];
    int nonceLen = privateKeyInput.read(nonce, 0, 11);
    System.out.print(nonceLen);
    byte[] bytes = new byte[1024];
    int num = privateKeyInput.read(bytes);

    SecretKeySpec passwordKey = new SecretKeySpec(password.getBytes(), "AES");
    Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
    c.init(Cipher.DECRYPT_MODE, passwordKey, spec);
    CipherOutputStream cipherOutput = new CipherOutputStream(output, c);


    while (num >= 0) {
        cipherOutput.write(bytes, 0, num);
        num = privateKeyInput.read(bytes);
    }

    cipherOutput.flush();
    cipherOutput.close();
    output.close();
    privateKeyInput.close();

    return decryptedFilename;
}
