public class ChaCha20Encryptor implements Encryptor {

    private final byte randomIvBytes[] = {0, 1, 2, 3, 4, 5, 6, 7};

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] randomKeyBytes) throws IOException, InvalidKeyException,
            InvalidAlgorithmParameterException, InvalidCipherTextException {

        ChaChaEngine cipher = new ChaChaEngine();
        CipherParameters cp = new KeyParameter(getMyKey(randomKeyBytes));
        cipher.init(true, new ParametersWithIV(cp , randomIvBytes));
        //cipher.init(true, new ParametersWithIV(new KeyParameter(randomKeyBytes), randomIvBytes));

        byte[] result = new byte[data.length];
        cipher.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] randomKeyBytes)
            throws InvalidKeyException, InvalidAlgorithmParameterException, IOException,
            IllegalStateException, InvalidCipherTextException {

        ChaChaEngine cipher = new ChaChaEngine();
        CipherParameters cp = new KeyParameter(getMyKey(randomKeyBytes));
        cipher.init(false, new ParametersWithIV(cp , randomIvBytes));
        //cipher.init(false, new ParametersWithIV(new KeyParameter(randomKeyBytes), randomIvBytes));

        byte[] result = new byte[data.length];
        cipher.processBytes(data, 0, data.length, result, 0);
        return result;
    }

    @Override
    public int getKeyLength() {
        return 32;
    }

    @Override
    public String toString() {
        return "ChaCha20()";
    }

    private static byte[] getMyKey(byte[] key){
        try {
            //byte[] key = encodekey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return key;
    }
}
