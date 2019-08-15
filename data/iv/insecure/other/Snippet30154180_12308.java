public class CryptTest{
    private static final String SALT = "HARcodedRandomSaltCharacters";
    private static final String IV = "somerandomcharacters";

    public static final String ALGORITHM = "AES";
    public static final String SEC_KEY_TYPE = "PBKDF2WithHmacSHA1";
    public static final String CIPHER = "AES/CBC/PKCS5Padding";
    public static final String ENCODING = "UTF-8";

    private static final byte[] getSalt(int size){
        return getBytes(SALT, size);
    }

    private static final byte[] getIv(int size){
        return getBytes(IV, size);
    }

    private static final byte[] getBytes(String s, int size){
        while (s.length() < size){
            s += s;
        }
        byte[] ret = new byte[size];
        System.arraycopy(s.getBytes(Charset.forName(ENCODING)), 0,  ret, 0, size);
        return ret;
}

    public static SecretKey genKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
             {
        final int iterationCount = 666;
        final int keyLength = 256;
        final int saltLength = keyLength / 8; // same size as key output


        byte[] salt = getSalt(saltLength);

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SEC_KEY_TYPE);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
        return key;

    }

    public static byte[] encrypt(String password){
        SecretKey key = genKey(password);

        Cipher cipher = Cipher.getInstance(CIPHER);
        int blockSize = cipher.getBlockSize();
        byte[] iv = getIv(blockSize);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        File inFile = new File("/path/to/test.txt");
        InputStream is = new InputStream(inFile);
        byte[] fileContents = new byte[(int)inFile.length()];
        is.read(fileContents);    

        return cipher.doFinal(fileContents);
    }
}
