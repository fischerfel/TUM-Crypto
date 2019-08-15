public class SimpleCryptoActivity extends Activity {
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            AssetManager am = this.getAssets();
            // get the encrypted image from assets folder
            InputStream is = am.open("2000_1.jpg_encrypted");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            byte[] b = new byte[IO_BUFFER_SIZE];  

            int read;
            //convert inputstream to bytearrayoutputstream
            while ((read = is.read(b)) != -1) {  
                baos.write(b, 0, read);
            }   
            byte[] key = "MARTIN_123_MARTIN_123".getBytes("UTF-8");
            byte[] iv = "1234567890123456".getBytes("UTF-8");

            long start = System.currentTimeMillis()/1000L; // start
            byte[] decryptedData = decrypt(key, iv, b);

            //END
            long end = System.currentTimeMillis()/1000L;    // end
            Log.d("TEST","Time start "+ String.valueOf(start));
            Log.d("TEST","Time end "+ String.valueOf(end));

            //decoding bytearrayoutputstream to bitmap
            Bitmap bitmap = 
                BitmapFactory.decodeByteArray(decryptedData,
                                              0,
                                              decryptedData.length);    

            int i = bitmap.getRowBytes() * bitmap.getHeight() ;

            TextView txt = (TextView) findViewById(R.id.text);
            txt.setText(String.valueOf(i));
            is.close(); // close the inputstream
            baos.close(); // close the bytearrayoutputstream
        } catch (Exception e) {
            e.fillInStackTrace();
            Log.e("error","err",e);
        }
    } 

    //decrypt
    private byte[] decrypt(byte[] raw, byte[] iv, byte[] encrypted)
            throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivspec = new IvParameterSpec(iv);         
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
        byte[] decrypted = cipher.doFinal(encrypted);

        return decrypted;
    }
}
