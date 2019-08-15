public Main3Activity() {
    try {
      //  SecureRandom sr = SecureRandom.getInstance("SHA1PRNG","Crypto");

        keyGenerator = KeyGenerator.getInstance("Blowfish");
        secretKey = keyGenerator.generateKey();
        cipher = Cipher.getInstance("Blowfish");
    } catch (NoSuchPaddingException ex) {
        System.out.println(ex);
    } catch (NoSuchAlgorithmException ex) {
        System.out.println(ex);
    }
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main3);


    ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    v1=(ImageView)findViewById(R.id.imageViewnew);
    b1=(Button)findViewById(R.id.cameranew);

    final String imagePath = "image" + ".png";
    File root = new File(Environment.getExternalStorageDirectory() + File.separator + "savedimages");
    root.mkdirs();
    if (!root.exists()) {
        root.mkdir();
    }

    file = new File(Environment.getExternalStorageDirectory() + File.separator + "savedimages",imagePath);
    file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "savedimages","Encryp"+imagePath);
    file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "savedimages","decryp"+imagePath);

    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            outputFileUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, CAMERA_REQUEST);

        }
    });
}
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


        OutputStream outStream = null;
        InputStream inStream = null;
        try {

            System.out.println("Encryption Over");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            inStream = new FileInputStream(file);
            outStream = new FileOutputStream(file1);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = inStream.read(buffer)) > 0) {
                outStream.write(cipher.update(buffer, 0, len));
                outStream.flush();
            }
            outStream.write(cipher.doFinal());
            outStream.close();
        } catch (IllegalBlockSizeException ex) {
            System.out.println(ex);
        } catch (BadPaddingException ex) {
            System.out.println(ex);
        } catch (InvalidKeyException ex) {
            System.out.println(ex);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }


        try {
            System.out.println("Decryption Over");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            inStream = new FileInputStream(file1);
            outStream = new FileOutputStream(file2);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = inStream.read(buffer)) > 0)
            {
            //------------>>>>>>>>>    Here Image is Storing instead of that it should read as byte array
                // ------------>>>>>>>>>>   and read as bitmap and set in Imageview for preview

                outStream.write(cipher.update(buffer, 0, len));
                outStream.flush();
            }
            System.out.print("Decryption ovverrr");
            outStream.write(cipher.doFinal());
            inStream.close();
            outStream.close();
        } catch (IllegalBlockSizeException ex) {
            System.out.println(ex);
        } catch (BadPaddingException ex) {
            System.out.println(ex);
        } catch (InvalidKeyException ex) {
            System.out.println(ex);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }


        BitmapFactory.Options options=new BitmapFactory.Options();
        final Bitmap b=BitmapFactory.decodeFile(file2.toString(),options);
        System.out.println("Image getting from File");

        //----------->>>>>>>>>> Decrypted Image should view Here. Currently I am getting directly from file.
        v1.setImageBitmap(b);

    }
}
}
