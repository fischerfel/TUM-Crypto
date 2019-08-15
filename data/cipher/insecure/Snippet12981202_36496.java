    //These will be used as the source of the configuration file's stored attributes.
    private static final Map<String, String> COMMON_ATTRIBUTES = new HashMap<String, String>();
    private static final Map<String, char[]> SECURE_ATTRIBUTES = new HashMap<String, char[]>();
    //Ciphering (encryption and decryption) password/key.
    private static final char[] PASSWORD = "Unauthorized_Personel_Is_Unauthorized".toCharArray();
    //Cipher salt.
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,};
    //Desktop dir:
    private static final File DESKTOP = new File(System.getProperty("user.home") + "/Desktop");
    //File names:
    private static final String NO_ENCRYPTION = "no_layers.txt";
    private static final String SINGLE_LAYER = "single_layer.txt";
    private static final String DOUBLE_LAYER = "double_layer.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws GeneralSecurityException, FileNotFoundException, IOException {
        //Set common attributes.
        COMMON_ATTRIBUTES.put("Gender", "Male");
        COMMON_ATTRIBUTES.put("Age", "21");
        COMMON_ATTRIBUTES.put("Name", "Hypot Hetical");
        COMMON_ATTRIBUTES.put("Nickname", "HH");

        /*
         * Set secure attributes.
         * NOTE: Ignore the use of Strings here, it's being used for convenience only.
         * In real implementations, JPasswordField.getPassword() would send the arrays directly.
         */
        SECURE_ATTRIBUTES.put("Username", "Hypothetical".toCharArray());
        SECURE_ATTRIBUTES.put("Password", "LetMePass_Word".toCharArray());

        /*
         * For demosntration purposes, I make the three encryption layer-levels I mention.
         * To leave no doubt the code works, I use real file IO.
         */
        //File without encryption.
        create_EncryptedFile(NO_ENCRYPTION, COMMON_ATTRIBUTES, SECURE_ATTRIBUTES, 0);
        //File with encryption to secure attributes only.
        create_EncryptedFile(SINGLE_LAYER, COMMON_ATTRIBUTES, SECURE_ATTRIBUTES, 1);
        //File completely encrypted, including re-encryption of secure attributes.
        create_EncryptedFile(DOUBLE_LAYER, COMMON_ATTRIBUTES, SECURE_ATTRIBUTES, 2);

        /*
         * Show contents of all three encryption levels, from file.
         */
        System.out.println("NO ENCRYPTION: \n" + readFile_NoDecryption(NO_ENCRYPTION) + "\n\n\n");
        System.out.println("SINGLE LAYER ENCRYPTION: \n" + readFile_NoDecryption(SINGLE_LAYER) + "\n\n\n");
        System.out.println("DOUBLE LAYER ENCRYPTION: \n" + readFile_NoDecryption(DOUBLE_LAYER) + "\n\n\n");

        /*
         * Decryption is demonstrated with the Double-Layer encryption file.
         */
        //Descrypt first layer. (file content) (REMEMBER: Layers are in reverse order from writing).
        String decryptedContent = readFile_ApplyDecryption(DOUBLE_LAYER);
        System.out.println("READ: [first layer decrypted]\n" + decryptedContent + "\n\n\n");
        //Decrypt second layer (secure data).
        for (String line : decryptedContent.split("\n")) {
            String[] pair = line.split(": ", 2);
            if (pair[0].equalsIgnoreCase("Username") || pair[0].equalsIgnoreCase("Password")) {
                System.out.println("Decrypted: " + pair[0] + ": " + decrypt(pair[1]));
            }
        }
    }

    private static String encrypt(byte[] property) throws GeneralSecurityException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

        //Encrypt and save to temporary storage.
        String encrypted = Base64.encodeBytes(pbeCipher.doFinal(property));

        //Cleanup data-sources - Leave no traces behind.
        for (int i = 0; i < property.length; i++) {
            property[i] = 0;
        }
        property = null;
        System.gc();

        //Return encryption result.
        return encrypted;
    }

    private static String encrypt(char[] property) throws GeneralSecurityException {
        //Prepare and encrypt.
        byte[] bytes = new byte[property.length];
        for (int i = 0; i < property.length; i++) {
            bytes[i] = (byte) property[i];
        }
        String encrypted = encrypt(bytes);

        /*
         * Cleanup property here. (child data-source 'bytes' is cleaned inside 'encrypt(byte[])').
         * It's not being done because the sources are being used multiple times for the different layer samples.
         */
//      for (int i = 0; i < property.length; i++) { //cleanup allocated data.
//          property[i] = 0;
//      }
//      property = null; //de-allocate data (set for GC).
//      System.gc(); //Attempt triggering garbage-collection.

        return encrypted;
    }

    private static String encrypt(String property) throws GeneralSecurityException {
        String encrypted = encrypt(property.getBytes());
        /*
         * Strings can't really have their allocated data cleaned before CG,
         * that's why secure data should be handled with char[] or byte[].
         * Still, don't forget to set for GC, even for data of sesser importancy;
         * You are making everything safer still, and freeing up memory as bonus.
         */
        property = null;
        return encrypted;
    }

    private static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(Base64.decode(property)));
    }

    private static void create_EncryptedFile(
                    String fileName,
                    Map<String, String> commonAttributes,
                    Map<String, char[]> secureAttributes,
                    int layers)
                    throws GeneralSecurityException, FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        for (String k : commonAttributes.keySet()) {
            sb.append(k).append(": ").append(commonAttributes.get(k)).append(System.lineSeparator());
        }
        //First encryption layer. Encrypts secure attribute values only.
        for (String k : secureAttributes.keySet()) {
            String encryptedValue;
            if (layers >= 1) {
                encryptedValue = encrypt(secureAttributes.get(k));
            } else {
                encryptedValue = new String(secureAttributes.get(k));
            }
            sb.append(k).append(": ").append(encryptedValue).append(System.lineSeparator());
        }

        //Prepare file and file-writing process.
        File f = new File(DESKTOP, fileName);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        } else if (f.exists()) {
            f.delete();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        //Second encryption layer. Encrypts whole file content including previously encrypted stuff.
        if (layers >= 2) {
            bw.append(encrypt(sb.toString().trim()));
        } else {
            bw.append(sb.toString().trim());
        }
        bw.flush();
        bw.close();
    }

    private static String readFile_NoDecryption(String fileName) throws FileNotFoundException, IOException, GeneralSecurityException {
        File f = new File(DESKTOP, fileName);
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static String readFile_ApplyDecryption(String fileName) throws FileNotFoundException, IOException, GeneralSecurityException {
        File f = new File(DESKTOP, fileName);
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine()).append(System.lineSeparator());
        }
        return decrypt(sb.toString());
    }
