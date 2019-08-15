static void encrypt() throws IOException, NoSuchAlgorithmException,
               NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        String file_en_path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Decrypted";
        File extStore = new File(file_en_path,"text.epub");
        FileInputStream fis = new FileInputStream(extStore);
        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        File extStore_enc = new File(file_en_path,"text_enc.epub");
        FileOutputStream fos = new FileOutputStream(extStore_enc);
        Log.d("encrypt--fis------------->>>>>>",""+fis);
        Log.d("encrypt--fos------------->>>>>>",""+fos);
        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),"AES");
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

static void decrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

     String file_de_path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Decrypted";
     File extStore = new File(file_de_path,"text_enc.epub");
        //File extStore = Environment.getExternalStorageDirectory();
       FileInputStream fis = new FileInputStream(extStore);
       File extStore_dec = new File(file_de_path,"text_dec.epub");
       FileOutputStream fos = new FileOutputStream(extStore_dec);
        //FileOutputStream fos = context.openFileOutput("fontsize_dec.txt",Context.MODE_PRIVATE);

        Log.d("decrypt--fis------------->>>>>>",""+fis);
        Log.d("decrypt--fos------------->>>>>>",""+fos);

        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),"AES");
        IvParameterSpec ivSpec = new IvParameterSpec("MyDifficultPassw".getBytes());
        Cipher cipher = Cipher.getInstance("AES");
        try {
            cipher.init(Cipher.DECRYPT_MODE, sks, ivSpec);
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];

        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(cis, "UTF-8"));
        /*line = reader.readLine();
        sb.append(line);
        Log.d("sb.append(line)--------------------------->",""+sb.append(line));*/
        while ((line = reader.readLine()) != null) {
            sb.append(line);
           // Log.d("sb.append(line)--------------------------->",""+sb.append(line));
           // Log.d("Line--------------------------->",""+line);
        }
        Log.d("sb.toSting-------------------->",""+sb.toString());
        while ((b = cis.read(d)) != -1) {
            //Log.d("cis.read(d)------------------------>>>>>>>",""+cis.read(d));
               fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
 }
