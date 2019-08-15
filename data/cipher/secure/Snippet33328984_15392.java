public static void main(String[] args) throws Exception {
    generateKeys();        
    RSA.rsaEncrypt("AES.key","RSA(AES).key");
    RSA.rsaDecrypt("RSA(AES).key","AES(RSA).key");
}

public static void generateKeys() throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048);
    KeyPair kp = kpg.genKeyPair();
    PublicKey publicKey = kp.getPublic();
    PrivateKey privateKey = kp.getPrivate();

    System.out.println("keys created");

    KeyFactory fact = KeyFactory.getInstance("RSA");
    RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
            RSAPublicKeySpec.class);
    RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
            RSAPrivateKeySpec.class);

    saveToFile("publicA.key", pub.getModulus(), pub.getPublicExponent());
    saveToFile("privateA.key", priv.getModulus(), priv.getPrivateExponent());

    System.out.println("keys saved");
}

public static void saveToFile(String fileName, BigInteger mod,
        BigInteger exp) throws IOException {
    ObjectOutputStream fileOut = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(fileName)));
    try {
        fileOut.writeObject(mod);
        fileOut.writeObject(exp);
    } catch (Exception e) {
        throw new IOException("Unexpected error");
    } finally {
        fileOut.close();
        System.out.println("Closed writing file.");
    }
}

// Return the saved key
static Key readKeyFromFile(String keyFileName) throws IOException {
    InputStream in = new FileInputStream(keyFileName);
    ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
            in));
    try {
        BigInteger m = (BigInteger) oin.readObject();
        BigInteger e = (BigInteger) oin.readObject();
        KeyFactory fact = KeyFactory.getInstance("RSA");
        if (keyFileName.startsWith("publicB")) {
            return fact.generatePublic(new RSAPublicKeySpec(m, e));
        } else {
            return fact.generatePrivate(new RSAPrivateKeySpec(m, e));
        }
    } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error", e);
    } finally {
        oin.close();
        System.out.println("Closed reading file.");
    }
}

// Use this PublicKey object to initialize a Cipher and encrypt some data
public static void rsaEncrypt(String file_loc, String file_des)
        throws Exception {
    byte[] data = new byte[32];
    int i;

    System.out.println("start encyption");

    Key pubKey = readKeyFromFile("publicB.key");
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    FileInputStream fileIn = new FileInputStream(file_loc);
    FileOutputStream fileOut = new FileOutputStream(file_des);
    CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);

    // Read in the data from the file and encrypt it
    while ((i = fileIn.read(data)) != -1) {
        cipherOut.write(data, 0, i);
    }

    // Close the encrypted file
    cipherOut.close();
    fileIn.close();

    System.out.println("encrypted file created");
}

// Use this PublicKey object to initialize a Cipher and decrypt some data
public static void rsaDecrypt(String file_loc, String file_des)
        throws Exception {
    byte[] data = new byte[32];
    int i;

    System.out.println("start decyption");

    Key priKey = readKeyFromFile("privateB.key");
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, priKey);

    FileInputStream fileIn = new FileInputStream(file_loc);
    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
    FileOutputStream fileOut = new FileOutputStream(file_des);

    // Write data to new file
    while ((i = cipherIn.read()) != -1) {
        fileOut.write(i);
    }

    // Close the file
    fileIn.close();
    cipherIn.close();
    fileOut.close();

    System.out.println("decrypted file created");
}
