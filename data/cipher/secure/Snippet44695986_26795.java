public class AssetDatabaseOpenHelper {

    private static final String DB_NAME = "dictionary.db";
    private static final String DB_NAME_ENCRYPTED = "dictionary.7z";

    private Context context;

    public AssetDatabaseOpenHelper(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_NAME);
        File dbFileEncrypted = context.getDatabasePath(DB_NAME_ENCRYPTED);

        if (!dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDatabase(File dbFile) throws IOException, IOException, NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException{

        Toast.makeText(context, "The language dictionary is getting copied to your local file. Please wait.\n This is done once when the app is ran for the first time.", Toast.LENGTH_LONG).show();

        InputStream is = context.getAssets().open(DB_NAME_ENCRYPTED);

        if (!dbFile.getParentFile().exists()) dbFile.getParentFile().mkdir();
        OutputStream os = new FileOutputStream(dbFile);

        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("mypassword".getBytes(),
                "AES_256");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES_256");

        cipher.init(Cipher.DECRYPT_MODE, sks);
        // Wrap the output stream
        CipherInputStream cis = new CipherInputStream(is, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            os.write(d, 0, b);
        }
        os.flush();
        os.close();
        is.close();
        Toast.makeText(context, "The application is ready for the first use .", Toast.LENGTH_LONG).show();


    }
}
