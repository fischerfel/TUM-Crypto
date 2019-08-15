public class AesFileIo {
 public final static String EOL = "\n";
 public static final String AES_ALGORITHM = "AES/CTR/NoPadding";
 public static final String PROVIDER = "BC"; 
 private static final SecretKeySpec secretKeySpec = 
  new SecretKeySpec(AES_KEY_128, "AES");
 private static final IvParameterSpec ivSpec = new IvParameterSpec(IV);

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
