 private void mDecrypt_File(FileInputStream fin, String outFile) throws Exception {
    FileOutputStream fout = new FileOutputStream(outFile);

    byte[] iv = new byte[16];
    byte[] salt = new byte[16];
    byte[] len = new byte[8];
    byte[] FC_TAGBuffer = new byte[8];

    Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);

    DataInputStream dis = new DataInputStream(fin);

    dis.read(iv, 0, 16);
    dis.read(salt, 0, 16);

    Rfc2898DeriveBytes rfc = new Rfc2898DeriveBytes(DEFAULT_PASSWORD, salt, F_ITERATIONS);
    SecretKey key = new SecretKeySpec(rfc.getBytes(32), "AES");

    //decryption code
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    CipherInputStream cIn = new CipherInputStream(dis, cipher);

    cIn.read(len, 0, 8);
    long lSize = getLong(len, 0);

    cIn.read(FC_TAGBuffer, 0, 8);

    byte[] tempFC_TAGBuffer = changeByteArray(FC_TAGBuffer, 0);//new byte[8];                           

    BigInteger ulong = new BigInteger(1, tempFC_TAGBuffer);

    if (!ulong.equals(FC_TAG)) {
        Exception ex = new Exception("Tags are not equal");
        throw ex;
    }

    byte[] bytes = new byte[BUFFER_SIZE];
    //determine number of reads to process on the file                          
    long numReads = lSize / BUFFER_SIZE;
    // determine what is left of the file, after numReads                   
    long slack = (long) lSize % BUFFER_SIZE;

    int read = -1;
    int value = 0;
    int outValue = 0;

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.reset();
    // read the buffer_sized chunks         
    for (int i = 0; i < numReads; ++i) {
        read = cIn.read(bytes, 0, bytes.length);
        fout.write(bytes, 0, read);
        md.update(bytes, 0, read);
        value += read;
        outValue += read;
    }
    // now read the slack                   
    if (slack > 0) {
        read = cIn.read(bytes, 0, (int) slack);
        fout.write(bytes, 0, read);
        md.update(bytes, 0, read);
        value += read;
        outValue += read;
    }
    fout.flush();
    fout.close();
    byte[] curHash = md.digest();

    byte[] oldHash = new byte[md.getDigestLength()];
    read = cIn.read(oldHash, 0, oldHash.length);
    if (oldHash.length != read || (!CheckByteArrays(oldHash, curHash))) {
        Exception ex = new Exception("File Corrupted!");
        throw ex;
    }
    if (outValue != lSize) {
        Exception ex = new Exception("File Sizes don't match!");
        throw ex;
    }
}
