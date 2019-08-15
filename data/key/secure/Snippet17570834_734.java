      public class Cryptography {

    private String encryptedFileName = "Enc_File2.txt";
    private static String algorithm = "AES";
    private static final int outputKeyLength = 256;
    static SecretKey yourKey = null;

    SQLiteDatabase database;
    DBHelper helper;
    Context context;

    //saveFile("Hello From CoderzHeaven testing :: Gaurav Wable");
    //decodeFile();

    public Cryptography (Context context) {
        this.context = context;
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
    }


    public String encryptString(String data) {
        char[] p = { 'p', 'a', 's', 's' };
        //SecretKey yourKey = null;
        byte[] keyBytes = null;
        byte[] filesBytes = null;
        try {
            if(this.yourKey == null) {
                Log.d("key", "instance null");
                Cursor cursor = database.query("assmain", new String[]{"keyAvailability"}, null, null, null, null, null);
                cursor.moveToFirst();
                if(cursor.getInt(cursor.getColumnIndex("keyAvailability")) == 1) {
                    Log.d("key", "exists in DB");
                    keyBytes = cursor.getBlob(cursor.getColumnIndex("key"));
                    cursor.close();
                    filesBytes = encodeFile(keyBytes, data.getBytes());
                } else {
                    Log.d("key", "generating");
                    this.yourKey = generateKey(p, generateSalt().toString().getBytes()); 
                    filesBytes = encodeFile(this.yourKey, data.getBytes());
                }
            } else {
                Log.d("key", "instance exists");
                //yourKey = this.yourKey;
                filesBytes = encodeFile(yourKey, data.getBytes());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new String(filesBytes);
    }

    public String decryptString(String data) {
        String str = null;
        byte[] decodedData = null;
        try {
            Log.d("To decrypt", data);
            if(this.yourKey == null) {
                Log.d("key", "null");
                Cursor cursor = database.query("assmain", new String[]{"keyAvailability"}, null, null, null, null, null);
                cursor.moveToFirst();
                if(cursor.getInt(cursor.getColumnIndex("keyAvailability")) == 1) {
                    Log.d("key", "exists in DB");
                    byte[] keyBytes = cursor.getBlob(cursor.getColumnIndex("key"));
                    cursor.close();
                    decodedData = decodeFile(keyBytes, data.getBytes());
                } else {
                    Log.d("key", "Unavailable");
                    Toast.makeText(context, "Key Unavailable", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("key", "instance exists");
                decodedData = decodeFile(this.yourKey, data.getBytes());
            }
            decodedData = decodeFile(yourKey, data.getBytes());
            str = new String(decodedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        //final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations,
                outputKeyLength);
        yourKey = secretKeyFactory.generateSecret(keySpec);
        return yourKey;
    }

    public static SecretKey generateSalt() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        //final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static byte[] encodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }

    public static byte[] encodeFile(byte[] data, byte[] fileData)
            throws Exception {
        //byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(fileData);

        return encrypted;
    }

    public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        //Cipher cipher = Cipher.getInstance(algorithm);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] decrypted = cipher.doFinal(fileData);

        return decrypted;
    }

    public static byte[] decodeFile(byte[] data, byte[] fileData)
            throws Exception {
        //byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] decrypted = cipher.doFinal(fileData);

        return decrypted;
    }
    }
