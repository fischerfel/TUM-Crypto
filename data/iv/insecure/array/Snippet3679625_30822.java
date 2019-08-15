public class FileIO {
    public final static String EOL = "\n";
    public static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    public static final String PROVIDER = "BC"; 
    private static final byte[] AES_KEY_128 = { // Hard coded for now
        78, -90, 42, 70, -5, 20, -114, 103, 
        -99, -25, 76, 95, -85, 94, 57, 54};
    private static final byte[] IV = { // Hard coded for now
        -85, -67, -5, 88, 28, 49, 49, 85, 
        114, 83, -40, 119, -65, 91, 76, 108};
    private static final SecretKeySpec secretKeySpec = 
        new SecretKeySpec(AES_KEY_128, "AES");
    private static final IvParameterSpec ivSpec = 
        new IvParameterSpec(IV);

    public String readAesFile(Context c, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = c.openFileInput(fileName);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            InputStreamReader isr = new InputStreamReader(cis);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(EOL);
            }
            is.close();
        } catch (java.io.FileNotFoundException e) {
            // OK, file probably not created yet
            Log.i(this.getClass().toString(), e.getMessage(), e);
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
        return stringBuilder.toString();
    }

    public void writeAesFile(Context c, String fileName, String theFile) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM, PROVIDER); 
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(theFile.getBytes()); 
            OutputStream os = c.openFileOutput(fileName, 0);
            os.write(encrypted);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
    }
}
