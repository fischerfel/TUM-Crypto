// RSA_INSTANCE = "RSA";
// ASSYM_CRYPTO_STR = 1024;
// SYM_CRYPTO_STR = 128;
// SYM_CRYPTO = "AES";
// AES_INSTANCE = "AES/CTR/NoPadding";
//
// File in = plain input file
// File out = encrypted output file
// Key pubKey = public Key (that wraps a random AES key)
public static void encryptFile(File in, File out, Key pubKey) throws Exception {
    FileInputStream fin;
    FileOutputStream fout;
    int nread = 0; 
    byte[] inbuf = new byte[1024];
    fout = new FileOutputStream(out);
    fin = new FileInputStream(in);

    SecureRandom random = new SecureRandom();
    // symmetric wrapping
    Key sKey = createKeyForAES(Config.SYM_CRYPTO_STR, random);
    IvParameterSpec sIvSpec = createCtrIvForAES(0, random);

    // encrypt symmetric key with RSA/pub key
    Cipher xCipher = Cipher.getInstance(Config.RSA_INSTANCE);
    xCipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
    byte[] keyBlock = xCipher.doFinal(packKeyAndIv(sKey, sIvSpec));

    fout.write(keyBlock);

    // encrypt data with symmetric key
    Cipher sCipher = Cipher.getInstance(Config.AES_INSTANCE);
    sCipher.init(Cipher.ENCRYPT_MODE, sKey, sIvSpec);

    // Now read our file and encrypt it.
    while((nread = fin.read(inbuf)) > 0) {
        fout.write(sCipher.update(inbuf, 0, nread)); // cannot be null, by construction
    }
    // NB doFinal() cannot return null, but can return a zero-length array, which is benign below.
    fout.write(sCipher.doFinal());

    fout.flush();
    fin.close();
    fout.close();
}


// Decrypt File
public static void decryptFile(File in, File out, Key privKey) throws Exception {
    FileInputStream fin;
    FileOutputStream fout;
    int nread = 0; 
    byte[] inbuf = new byte[1024];
    fout = new FileOutputStream(out);
    fin = new FileInputStream(in);

    byte[] keyBlock = new byte[128];
    nread = fin.read(keyBlock);

    Cipher xCipher = Cipher.getInstance(Config.RSA_INSTANCE);
    Cipher sCipher = Cipher.getInstance(Config.AES_INSTANCE);   

    // symmetric key/iv unwrapping step
    xCipher.init(Cipher.DECRYPT_MODE, privKey);
    Object[] keyIv = unpackKeyAndIV(xCipher.doFinal(keyBlock));

    // decryption step
    sCipher.init(Cipher.DECRYPT_MODE, (Key)keyIv[0], (IvParameterSpec)keyIv[1]);

    while((nread = fin.read(inbuf)) >0) {
        fout.write(sCipher.update(inbuf,0,nread));
    }
    fout.write(sCipher.doFinal());

    fout.flush();
    fin.close();
    fout.close();

}

public static byte[] packKeyAndIv(Key key, IvParameterSpec ivSpec) throws IOException {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    bOut.write(ivSpec.getIV());
    bOut.write(key.getEncoded());
    return bOut.toByteArray();
}

public static Object[] unpackKeyAndIV(byte[] data) {
    byte[] keyD = new byte[16];
    byte[] iv = new byte[data.length - 16];

    return new Object[] {
        new SecretKeySpec(data, 16, data.length - 16, "AES"),
        new IvParameterSpec(data, 0, 16)
    };
}
