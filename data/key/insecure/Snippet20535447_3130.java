   @Override
   protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);

          Button encryptButton = (Button) findViewById(R.id.button1);
          Button DecryptButton = (Button) findViewById(R.id.button2);
          encryptButton.setOnClickListener(new OnClickListener() {

                 @Override
                 public void onClick(View v) {
                       // TODO Auto-generated method stub
                       try {
                              encrypt();
                       } catch (InvalidKeyException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       } catch (NoSuchAlgorithmException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       } catch (NoSuchPaddingException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       } catch (IOException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       }
                 }
          });

          DecryptButton.setOnClickListener(new OnClickListener() {

                 @Override
                 public void onClick(View v) {
                       // TODO Auto-generated method stub
                       try {
                              decrypt();
                       } catch (InvalidKeyException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       } catch (NoSuchAlgorithmException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       } catch (NoSuchPaddingException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       } catch (IOException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                       }
                 }
          });

   }

   /**
    * Here is Both function for encrypt and decrypt file in Sdcard folder. we
    * can not lock folder but we can encrypt file using AES in Android, it may
    * help you.
    *
    * @throws IOException
    * @throws NoSuchAlgorithmException
    * @throws NoSuchPaddingException
    * @throws InvalidKeyException
    */

   static void encrypt() throws IOException, NoSuchAlgorithmException,
                 NoSuchPaddingException, InvalidKeyException {
          // Here you read the cleartext.
          File extStore = Environment.getExternalStorageDirectory();
          FileInputStream fis = new FileInputStream(extStore + "/sampleFile");
          // This stream write the encrypted text. This stream will be wrapped by
          // another stream.
          FileOutputStream fos = new FileOutputStream(extStore + "/encrypted");

          // Length is 16 byte
          SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                       "AES");
          // Create cipher
          Cipher cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.ENCRYPT_MODE, sks);
          // Wrap the output stream
          CipherOutputStream cos = new CipherOutputStream(fos, cipher);
          // Write bytes
          int b;
          byte[] d = new byte[8];
          while ((b = fis.read(d)) != -1) {
                 cos.write(d, 0, b);
          }
          // Flush and close streams.
          cos.flush();
          cos.close();
          fis.close();
   }

   static void decrypt() throws IOException, NoSuchAlgorithmException,
                 NoSuchPaddingException, InvalidKeyException {

          File extStore = Environment.getExternalStorageDirectory();
          FileInputStream fis = new FileInputStream(extStore + "/encrypted");

          FileOutputStream fos = new FileOutputStream(extStore + "/decrypted");
          SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                       "AES");
          Cipher cipher = Cipher.getInstance("AES");
          cipher.init(Cipher.DECRYPT_MODE, sks);
          CipherInputStream cis = new CipherInputStream(fis, cipher);
          int b;
          byte[] d = new byte[8];
          while ((b = cis.read(d)) != -1) {
                 fos.write(d, 0, b);
          }
          fos.flush();
          fos.close();
          cis.close();
   }
