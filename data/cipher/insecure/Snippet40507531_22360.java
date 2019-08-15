 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         e1 = (EditText)findViewById(R.id.editText1);
        e2 = (EditText)findViewById(R.id.editText2);
        e3 = (EditText)findViewById(R.id.editText3);
        e4 = (EditText)findViewById(R.id.editText4);
        e5 = (EditText)findViewById(R.id.editText5);
        final JSONObject obj = new JSONObject();
        sub = (Button)findViewById(R.id.button);
        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Outer_Folder");
        file.mkdir();            // store and fetch file from storage...
        f1  = new File(file,"null"+".txt");
        f2  = new File(fileD,"null"+".txt");
        try {
            SecureRandom srm = SecureRandom.getInstance("SHA1PRNG");
            srm.setSeed("no one can read".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128,srm);
            sec = kg.generateKey();
           bb1 = sec.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (f1.exists()){
            File file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Outer_Folder");
            f4  = new File(file3,"null"+".txt");
            int size = (int)f4.length();
            contents = new byte[size];
            try {
                 bufferedInputStream = new BufferedInputStream(new FileInputStream(f4));
                try {
                    bufferedInputStream.read(contents);
                    bufferedInputStream.close();
                    String dd = new String(contents);
                    SecretKeySpec sdf = new SecretKeySpec(bb1,"AES");
                    Cipher cv = Cipher.getInstance("AES");
                    cv.init(Cipher.DECRYPT_MODE,sdf);
                    decodefile = cv.doFinal(dd.getBytes());         // aes decryption not working 
                    String ggg = new String(decodefile);
                    Toast.makeText(getApplicationContext(),ggg,Toast.LENGTH_SHORT).show();
                    JSONObject jsonObj = new JSONObject(ggg);
                    v1=jsonObj.getString("one");
                    v2=jsonObj.getString("two");                 // when enter the activity json filled by decryped file
                    v3=jsonObj.getString("three");
                    v4=jsonObj.getString("four");
                    v5=jsonObj.getString("five");
                    e1.setText(v1);
                    e2.setText(v2);
                    e3.setText(v3);
                    e4.setText(v4);
                    e5.setText(v5);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                 catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } }else {
            Toast.makeText(getApplicationContext(),"con't read",Toast.LENGTH_LONG).show();
        } sub.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();
                s2=e2.getText().toString();
                s3=e3.getText().toString();
                s4=e4.getText().toString();
                s5=e5.getText().toString();
                try {
                    obj.put("one", s1);
                    obj.put("two", s2);
                    obj.put("three", s3);          //converting json 
                    obj.put("four", s4);
                    obj.put("five", s5);
                    if (f1.exists()){ //
                        f1.delete();
                    }
                     vv = obj.toString();
                    new execute();

                    try {
                        SecretKeySpec sksep = new SecretKeySpec(bb1,"AES");
                        Cipher cp = Cipher.getInstance("AES");
                        cp.init(Cipher.ENCRYPT_MODE,sksep);
                        encobyte = cp.doFinal(vv.getBytes());  // aes encryption  - working
                        FileOutputStream uu = null;
                        uu = new FileOutputStream(f1);
                        uu.write(encobyte);
                        oo = new String(encobyte);
                        Toast.makeText(getApplicationContext(),oo,Toast.LENGTH_LONG).show();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {    //
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();            //
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
