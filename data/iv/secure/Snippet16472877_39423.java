private Spinner spinner;
private SpinAdapter adapter;
private Cipher cipher;
private ArrayList<ConnectionProfile> connectionProfiles;
private KeyGenerator keygen;
private SecretKey key;
private String salt;
private SecretKey saltedKey;
private static final String RANDOM_ALGORITHM = "SHA1PRNG";
private IvParameterSpec ivSpec;

public void createKey() throws Exception {
    keygen = KeyGenerator.getInstance("AES");
    key = keygen.generateKey();
    byte[] saltedKeyBytes = new byte[key.getEncoded().length+salt.getBytes().length];
    System.arraycopy(key.getEncoded(), 0, saltedKeyBytes, 0, key.getEncoded().length);
    System.arraycopy(salt.getBytes(), 0, saltedKeyBytes, key.getEncoded().length, salt.getBytes().length);
    saltedKey = new SecretKeySpec(saltedKeyBytes, 0, saltedKeyBytes.length, "AES");
}

 private byte[] generateIv() throws NoSuchAlgorithmException {
      SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
      byte[] iv = new byte[16];
      random.nextBytes(iv);
      return iv;
}

public void saveProfiles() {
    try {
        if (key == null) {createKey();}
        cipher.init(Cipher.ENCRYPT_MODE, saltedKey, ivSpec);
        FileOutputStream fos = openFileOutput("connProfiles.bin", Context.MODE_PRIVATE);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(connectionProfiles);
        oos.flush();
        oos.close();
        FileOutputStream keyOutputStream = openFileOutput("key.bin", Context.MODE_PRIVATE);
        keyOutputStream.write(key.getEncoded());
        keyOutputStream.flush();
        keyOutputStream.close();
        byte[] iv = generateIv();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        FileOutputStream ivOutputStream = openFileOutput("iv.bin", Context.MODE_PRIVATE);
        ivOutputStream.write(iv);
        ivOutputStream.flush();
        ivOutputStream.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void readProfiles() {
    try {
        File file = new File(this.getFilesDir(), "key.bin");
        byte[] keyBytes = new byte[(int) file.length()];
        FileInputStream keyInputStream = new FileInputStream(file);
        keyInputStream.read(keyBytes);
        keyInputStream.close();
        File file2 = new File(this.getFilesDir(), "iv.bin");
        byte[] iv = new byte[(int) file2.length()];
        FileInputStream ivInputStream = new FileInputStream(file2);
        ivInputStream.read(iv);
        ivInputStream.close();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        byte[] saltedKeyBytes = new byte[keyBytes.length+salt.getBytes().length];
        System.arraycopy(keyBytes, 0, saltedKeyBytes, 0, keyBytes.length);
        System.arraycopy(salt.getBytes(), 0, saltedKeyBytes, keyBytes.length, salt.getBytes().length);
        saltedKey = new SecretKeySpec(saltedKeyBytes, 0, saltedKeyBytes.length, "AES");
        cipher.init(Cipher.DECRYPT_MODE, saltedKey, ivSpec);
        FileInputStream fis = openFileInput("connProfiles.bin");
        BufferedInputStream bis = new BufferedInputStream(fis);
        CipherInputStream cis = new CipherInputStream(bis, cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);
        connectionProfiles = (ArrayList<ConnectionProfile>) ois.readObject();
        ois.close();
    } catch (Exception e) {
        e.printStackTrace();
        ;
    }
}
