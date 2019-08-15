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
                //START
                long start = System.currentTimeMillis()/1000L; // start

                //byte[] keyStart = "MARTIN_123_MARTIN_123".getBytes();  // specific key value 
                KeyGenerator kgen = KeyGenerator.getInstance("AES/CBC/PKCS5Padding");   //aes
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                //sr.setSeed(keyStart);
                kgen.init(128, sr); 
                //SecretKey skey = kgen.generateKey();
                //byte[] key = skey.getEncoded();    

                byte[] key = "MARTIN_123_MARTIN_123".getBytes("UTF-8");
                byte[] iv = "1234567890123456".getBytes("UTF-8");
                byte[] decryptedData = decrypt(key, iv, b);


                //END
                long end = System.currentTimeMillis()/1000L;    // end
                Log.d("TEST","Time start "+ String.valueOf(start)); //showing the strat in ms
                Log.d("TEST","Time end "+ String.valueOf(end));     //showing the end in ms

                Bitmap bitmap = BitmapFactory.decodeByteArray(decryptedData , 0, decryptedData .length);    //decoding bytearrayoutputstream to bitmap
                //String filepath = Environment.getExternalStorageDirectory()+"bitmap";
                FileOutputStream fos = new FileOutputStream("sdcard/DCIM/100ANDRO");
                fos.write(decryptedData);
                fos.close();

                is.close(); // close the inputstream
                baos.close(); // close the bytearrayoutputstream
            }
            catch(Exception e){
                e.fillInStackTrace();
            }
        } 

        //decrypt
        private byte[] decrypt(byte[] raw, byte[] iv, byte[] encrypted) throws Exception {
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec ivspec = new IvParameterSpec(iv);         
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
      byte[] decrypted = cipher.doFinal(encrypted);

      return decrypted;
    }
