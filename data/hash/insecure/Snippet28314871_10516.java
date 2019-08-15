public static void appendAES(File file, byte[] data, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    RandomAccessFile rfile = new RandomAccessFile(file,"rw");
    byte[] iv = new byte[16];
    byte[] lastBlock = null;
    if (rfile.length() % 16L != 0L) {
        throw new IllegalArgumentException("Invalid file length (not a multiple of block size)");
    } else if (rfile.length() == 16) {
        throw new IllegalArgumentException("Invalid file length (need 2 blocks for iv and data)");
    } else if (rfile.length() == 0L) { 
        // new file: start by appending an IV
        new SecureRandom().nextBytes(iv);
        rfile.write(iv);
        // we have our iv, and there's no prior data to reencrypt
    } else { 
        // file length is at least 2 blocks
        rfile.seek(rfile.length()-32); // second to last block
        rfile.read(iv); // get iv
        byte[] lastBlockEnc = new byte[16]; 
            // last block
            // it's padded, so we'll decrypt it and 
            // save it for the beginning of our data
        rfile.read(lastBlockEnc);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(iv));
        lastBlock = cipher.doFinal(lastBlockEnc);
        rfile.seek(rfile.length()-16); 
            // position ourselves to overwrite the last block
    } 
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(iv));
    byte[] out;
    if (lastBlock != null) { // lastBlock is null if we're starting a new file
        out = cipher.update(lastBlock);
        if (out != null) rfile.write(out);
    }
    out = cipher.doFinal(data);
    rfile.write(out);
    rfile.close();
}

public static void decryptAES(File file, OutputStream out, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    // nothing special here, decrypt as usual
    FileInputStream fin = new FileInputStream(file);
    byte[] iv = new byte[16];
    if (fin.read(iv) < 16) {
        throw new IllegalArgumentException("Invalid file length (needs a full block for iv)");
    };
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(iv));
    byte[] buff = new byte[1<<13]; //8kiB
    while (true) {
        int count = fin.read(buff);
        if (count == buff.length) {
            out.write(cipher.update(buff));
        } else {
            out.write(cipher.doFinal(buff,0,count));
            break;
        }
    }
    fin.close();
}

public static void main(String[] args) throws Exception {

    // prep the new encrypted output file reference
    File encryptedFileSpec = File.createTempFile("chunked_aes_encrypted.", ".test");

    // prep the new decrypted output file reference
    File decryptedFileSpec = File.createTempFile("chunked_aes_decrypted.", ".test");

    // generate a key spec 
    byte[] keySpec = new byte[]{0,12,2,8,4,5,6,7, 8, 9, 10, 11, 12, 13, 14, 15};

    // for debug/test purposes only, keep track of what's written 
    StringBuilder plainTextLog = new StringBuilder();

    // perform chunked output
    for (int i = 0; i<1000; i++) {

        // generate random text of variable length
        StringBuilder text = new StringBuilder();
        Random rand = new Random();
        int  n = rand.nextInt(5) + 1;
        for (int j = 0; j < n; j++) {
            text.append(UUID.randomUUID().toString()); // append random string
        }

        // record it for later comparison
        plainTextLog.append(text.toString());

        // write it out
        byte[] b = text.toString().getBytes("UTF-8");
        appendAES(encryptedFileSpec, b, keySpec);
    }

    System.out.println("Encrypted " + encryptedFileSpec.getAbsolutePath());

    // decrypt
    decryptAES(encryptedFileSpec, new FileOutputStream(decryptedFileSpec), keySpec);
    System.out.println("Decrypted " + decryptedFileSpec.getAbsolutePath());

    // compare expected output to actual
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] expectedDigest = md.digest(plainTextLog.toString().getBytes("UTF-8"));

    byte[] expectedBytesEncoded = Base64.getEncoder().encode(expectedDigest);
    System.out.println("Expected decrypted content: " + new String(expectedBytesEncoded));

    byte[] actualBytes = Files.readAllBytes(Paths.get(decryptedFileSpec.toURI()));
    byte[] actualDigest = md.digest(actualBytes);
    byte[] actualBytesEncoded = Base64.getEncoder().encode(actualDigest);
    System.out.println("> Actual decrypted content: " + new String(actualBytesEncoded));


}
