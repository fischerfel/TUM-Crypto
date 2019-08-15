private static final int KEY_LEN = 16;
private static final String ENCRYPTION_ALGO = "AES";
private static final String HASHING_ALGO = "SHA-256"; 
private static final String CIPHER_MODE = "AES/CFB/PKCS5Padding";
private static final String ENCODING = "UTF-8";

static private byte[] initVector;
static private SecretKey key;
static Cipher cipher;

private static SecretKey generateKey(byte[] password) {
    MessageDigest md = MessageDigest.getInstance(HASHING_ALGO);

    byte[] hashedKey = md.digest(password);

    return new SecretKeySpec(hashedKey, 0, KEY_LEN, ENCRYPTION_ALGO);
}

public static byte[] getIV() {
    return initVector;
}

public static void setup(String password) 
{

    key = generateKey(password.getBytes(ENCODING));

    cipher = Cipher.getInstance (CIPHER_MODE);
    cipher.init (Cipher.ENCRYPT_MODE, key);
    AlgorithmParameters params = cipher.getParameters ();

    initVector = params.getParameterSpec (IvParameterSpec.class).getIV();
}

public static void setup(String password, byte[] iv)
{
    key = generateKey(password.getBytes(ENCODING));

    cipher = Cipher.getInstance (CIPHER_MODE);

    // define init vector that was used for encryption
    cipher.init (Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
}

private static byte[] crypt(byte[] plaintext, boolean hasNext) 
    if (hasNext) {
        return cipher.update(plaintext);
    }else {
        return cipher.doFinal(plaintext);
    }
}

public static void main(String [] args)
{
    try {
        File input = new File ("input.txt");
        File eoutput = new File ("output.enc");
        File doutput = new File ("decrypted.txt");

        FileInputStream fin = new FileInputStream(input);
        FileOutputStream fout = new FileOutputStream (eoutput);

        byte [] buffer = new byte [32];
        int nBytes;

        //encrypt
        setup("password");

        while ((nBytes = fin.read(buffer))!=-1) {
            byte[] cyphertext;
            if (nBytes < buffer.length) {
                byte[] trimbuffer = new byte [nBytes];
                System.arraycopy(buffer, 0, trimbuffer, 0, nBytes);
                cyphertext = crypt(trimbuffer, false);
            }else {
                cyphertext = crypt(buffer, true);
            }
            fout.write(cyphertext);
        }
        fin.close();
        fout.close();

        FileInputStream fin2 = new FileInputStream(eoutput);
        FileOutputStream fout2 = new FileOutputStream (doutput);            

        byte[] iv = getIV();

        // decrypt
        setup("password",iv);
        while ((nBytes = fin2.read(buffer))!=-1) {
            byte[] plaintext;
            if (nBytes < buffer.length) {
                byte[] trimbuffer = new byte [nBytes];
                System.arraycopy(buffer, 0, trimbuffer, 0, nBytes);

                plaintext = crypt(trimbuffer, false);
            }else {
                plaintext = crypt(buffer, true);
            }
            fout2.write(plaintext);
        }

        fin2.close();
        fout2.close();

  }
