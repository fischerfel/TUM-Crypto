package com.aujas.html.viewer.content;
public class LocalFileContentProvider extends ContentProvider {

private static final String URI_PREFIX = "content://com.aujas.html.viewer.localfile.dec/";
public static File file;
public String filename;
public ParcelFileDescriptor[] parcels;

public static String constructUri(String url) {
    String editString = url.replaceAll("%20", " ");
    int n = editString.length();
    String uri = editString.substring(5, n - 1);
    Log.d("URI", uri);
    return URI_PREFIX + uri + "\"";
}

public ParcelFileDescriptor openFile(Uri uri, String mode) {

    Log.d("OPEN", uri.getPath());
    return parcels[0];

}

@Override
public boolean onCreate() {
    return true;
}

@Override
public int delete(Uri uri, String s, String[] as) {
    throw new UnsupportedOperationException(
            "Not supported by this provider");
}

@Override
public String getType(Uri uri) {
    throw new UnsupportedOperationException(
            "Not supported by this provider");
}

@Override
public Uri insert(Uri uri, ContentValues contentvalues) {
    throw new UnsupportedOperationException(
            "Not supported by this provider");
}

@Override
public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {
    throw new UnsupportedOperationException(
            "Not supported by this provider");
}

@Override
public int update(Uri uri, ContentValues contentvalues, String s,
        String[] as) {
    throw new UnsupportedOperationException(
            "Not supported by this provider");
}

class DecryptAsync extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... paramArrayOfParams) {
        // TODO Auto-generated method stub
        try {
            file = new File(paramArrayOfParams[0]);
            Log.d("DecrypOpened", file.toString());
            parcels = ParcelFileDescriptor.createPipe();
            Log.d("filebeindec", LocalFileContentProvider.file.toString());
            FileInputStream fis = new FileInputStream(LocalFileContentProvider.file);

            android.os.ParcelFileDescriptor.AutoCloseOutputStream out = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(parcels[1]);
            Cipher ecipher;
            Cipher dcipher;
            SecretKey key;
            String input = "768f8a949de079da";
            byte[] encoded = new BigInteger(input, 16).toByteArray();
            key = new SecretKeySpec(encoded, "DES");
            byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
                    0x07, 0x72, 0x6F, 0x5A };
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            byte[] buf = new byte[1024];
            InputStream in = new CipherInputStream(fis, dcipher);
            int numRead = 0;
            int n = 1;
            while ((numRead = in.read(buf)) >= 0) {
                n++;
                out.write(buf, 0, numRead);
                Log.d("Error", "SD");
                if (n == 64) {
                    out.flush();
                    out.flush();
                    n = 0;
                }
            }

            Log.d("Decypt Done", out.toString());
        } catch (Exception e) {
            Log.d("AsyncError", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

}
