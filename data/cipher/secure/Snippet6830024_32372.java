public class SimpleCryptoActivity extends Activity {
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

                try {
                    AssetManager am = this.getAssets();
                    InputStream is = am.open("2000_1.jpg_encrypted"); // get the encrypted image from assets folder

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                    byte[] b = new byte[IO_BUFFER_SIZE];  
                    int read;  
                    while ((read = is.read(b)) != -1) {  //convert inputstream to bytearrayoutputstream
                        baos.write(b, 0, read);
                    }                           
                long start = System.currentTimeMillis()/1000L; // start

                    byte[] keyStart = "MARTIN_123_MARTIN_123".getBytes();  // specific key value 
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");    //aes
                    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                    sr.setSeed(keyStart);
                    kgen.init(128, sr); 
                    SecretKey skey = kgen.generateKey();
                    byte[] key = skey.getEncoded();    

                    byte[] decryptedData = decrypt(key,b);  //decryption
                long end = System.currentTimeMillis()/1000L;    // end
                Log.i("TEST","Time start "+ String.valueOf(start)); //showing the strat in ms
                Log.i("TEST","Time end "+ String.valueOf(end));     //showing the end in ms
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0, b .length);    //decoding bytearrayoutputstream to bitmap



                    is.close(); // close the inputstream
                    baos.close(); // close the bytearrayoutputstream
                }
                catch(Exception e){
                    e.fillInStackTrace();
                }
            } 

            //decrypt
            private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] decrypted = cipher.doFinal(encrypted);

            return decrypted;
            }
}
