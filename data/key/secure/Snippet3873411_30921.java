public class AesFileIo {
    private static final String EOL = "\n";
    private static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivSpec;
    private static final String PROVIDER = "BC"; 

    AesFileIo(byte[] aesKey, byte[] iv) {
        ivSpec = new IvParameterSpec(iv);
        secretKeySpec = new SecretKeySpec(aesKey, "AES");
    }

    public String readFile(Context c, String fileName) {
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

    public void writeFile(Context c, String fileName, String theFile) {
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
