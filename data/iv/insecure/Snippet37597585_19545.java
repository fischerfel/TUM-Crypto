public void fileTest(File source, File output, String key) throws Exception {
    Log.write("Starting file encryption/decryption test!");
    Util.charset = CharsetToolkit.guessEncoding(source, 4096, StandardCharsets.UTF_8);
    Log.write("Using charset " + Util.charset.name());
    Log.write("Using key: " + key);
    String oHash = Util.checksum(source);
    Log.write("Original hash: " + oHash);

    //Cipher setup

    SecretKeySpec sks = Util.padKey(key);
    Cipher eCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    Cipher dCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    eCipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(new byte[16]));
    dCipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(new byte[16]));

    //IO setup
    File tmpEncrypt = new File(source.getParent() + "/" + source.getName() + "-tmp");
    tmpEncrypt.createNewFile();
    output.createNewFile();
    Log.write("Encrypting to: " + tmpEncrypt.getAbsolutePath());
    InputStream fis = new FileInputStream(source);
    InputStream enIn = new FileInputStream(tmpEncrypt);
    OutputStream fos = new FileOutputStream(tmpEncrypt);
    OutputStream clearOut = new FileOutputStream(output);
    CipherInputStream cis = new CipherInputStream(enIn, dCipher);
    CipherOutputStream cos = new CipherOutputStream(fos, eCipher);

    //Encrypt
    Log.write("Starting encryption process");
    int numRead = 0;
    byte[] buffer = new byte[1024];
    while ((numRead = fis.read(buffer)) >= 0) {
        cos.write(buffer, 0, numRead);
    }

    cos.close();
    fos.close();
    Log.write("Done!");

    Log.write("Encrypted hash: " + Util.checksum(output));

    //Decrypt
    Log.write("Starting decryption process");
    int nr = 0;
    byte[] b = new byte[1024];
    while ((nr = cis.read(b)) >= 0) {
        clearOut.write(buffer, 0, nr);
    }
    clearOut.close();
    cis.close();
    fis.close();
    Log.write("Done!");

    String fHash = Util.checksum(output);
    Log.write("Final hash: " + fHash);

    if(fHash.equals(oHash)) {
        Log.write("Success! The hashes are equal!");
    } else {
        Log.write("Failure! Hashes are different!");
    }
}
