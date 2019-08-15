public static boolean exportDB(int wellnessDayId) {
    Serializer serializer = new Persister();
    WellnessDay backToXMLWellnesday = ExportDbToXml.buildXmlDataModel(wellnessDayId);
    if (backToXMLWellnesday == null) {
        return false;
    }
    StringWriter sw = new StringWriter();
    FileOutputStream fos = null;
    FileOutputStream fosbackup = null;
    try {
        serializer.write(backToXMLWellnesday, sw);
        DesHelper des = new DesHelper("12345678");
        byte[] decryptedBytes = sw.toString().getBytes();

        //Calculating the size that the array should be (ie multiples of 8)
        Double len = Math.ceil(((double) decryptedBytes.length) / EIGHT) * EIGHT;
        byte[] decryptedBytesPadded = new byte[len.intValue()];

        //Initializing to whitespace character
        byte whiteSpaceBytevalue = 32;
        for (int k=0; k< decryptedBytesPadded.length; k++){
            decryptedBytesPadded[k]= whiteSpaceBytevalue;
        }

        //Copying the array into the byte array that is the correct length (ie multiples of 8)
        System.arraycopy(decryptedBytes, 0, decryptedBytesPadded, 0, Math.min(decryptedBytes.length, len.intValue()));

        byte[] encryptedBytes = des.encrypt(decryptedBytesPadded);
        String filename = "Export.blb";
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/export");
        dir.mkdirs();

        File file = new File(dir, filename);
        file.createNewFile();
        fos = new FileOutputStream(file);

        for (int p = 0; p < encryptedBytes.length; p++) {
            fos.write(encryptedBytes[p]);
        }

        Format formatter;
        Date date = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String dateFormatted = formatter.format(date);
        //Create Backup file in /Wellnessdays/archive
         File dirBackup = new File(sdCard.getAbsolutePath() + "/archive");
        dirBackup.mkdirs();
        File filebackup = new File(dirBackup, dateFormatted + " - " + filename);
        filebackup.createNewFile();
        fosbackup = new FileOutputStream(filebackup);
        for (int p = 0; p < encryptedBytes.length; p++) {
            fosbackup.write(encryptedBytes[p]);
        }


    } catch (FileNotFoundException e) {
        Log.e(TAG, e.getMessage());
        return false;
    } catch (IOException e) {
        Log.e(TAG, e.getMessage());
        return false;
    } catch (GeneralSecurityException e) {
        Log.e(TAG, e.getMessage());
        return false;
    } catch (Exception e) {
        Log.e(TAG, e.getMessage());
        return false;
    } finally {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e("DatabaseExportToXML", e.getMessage());
            }
        }
        if (fosbackup!=null){
            try {
                fosbackup.close();
            } catch (IOException e) {
                Log.e("DatabaseExportToXML", e.getMessage());
            }
        }
    }
    return true;
}

public class DesHelper {

public static int MAX_KEY_LENGTH = DESKeySpec.DES_KEY_LEN;
//private static String ENCRYPTION_ALGORITHM = "DES/CBC/PKCS5Padding";
private static String ENCRYPTION_ALGORITHM = "DES/ECB/NoPadding";
private static String ENCRYPTION_KEY_TYPE = "DES";

private final SecretKeySpec keySpec;

public DesHelper(String passphrase) {
    byte[] key;
    try {
        key = passphrase.getBytes("UTF8");
    } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException(e);
    }
    //The key is always 8 bytes, no need to pad the key
    keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
}

private byte[] padKeyToLength(byte[] key, int len) {
    byte[] newKey = new byte[len];
    System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
    return newKey;
}

public byte[] encrypt(byte[] unencrypted) throws GeneralSecurityException {
    return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
}

public byte[] decrypt(byte[] encrypted) throws GeneralSecurityException {
    return doCipher(encrypted, Cipher.DECRYPT_MODE);
}

private byte[] doCipher(byte[] original, int mode) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(mode, keySpec);
    return cipher.doFinal(original);
}

}
