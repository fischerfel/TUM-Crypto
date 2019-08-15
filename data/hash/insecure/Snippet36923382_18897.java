 pt = (EditText) findViewById(R.id.pt);
        lat = (EditText) findViewById(R.id.lat);
        lon = (EditText) findViewById(R.id.lon);
        ct = (TextView) findViewById(R.id.ct);
        b1 = (Button) findViewById(R.id.b1);

       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String plaintext = pt.getText().toString();
               String latitude = lat.getText().toString();
               String longitude = lon.getText().toString();

               String key = latitude;
               key = key.concat(longitude);
               byte[] array = new byte[0];
               try {
                   array = key.getBytes("UTF-8");
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }

               MessageDigest sha = null;
               try {
                   sha = MessageDigest.getInstance("SHA-1");
               } catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
               }
               array = sha.digest(array);
               array = Arrays.copyOf(array, 16); // use only first 128 bit
               Cipher cipher = null;
               try {
                   cipher = Cipher.getInstance("AES");
               } catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
               } catch (NoSuchPaddingException e) {
                   e.printStackTrace();
               }
               SecretKeySpec secretKeySpec = new SecretKeySpec(array, "AES");

               try {
                   cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
               } catch (InvalidKeyException e) {
                   e.printStackTrace();
               }
               byte[] encrypted = new byte[0];
               try {
                   encrypted = cipher.doFinal((plaintext).getBytes());
               } catch (IllegalBlockSizeException e) {
                   e.printStackTrace();
               } catch (BadPaddingException e) {
                   e.printStackTrace();
               }
               String ciphertext = Base64.encodeToString(encrypted,0);
               ct.setText(ciphertext);

           }
       });
    }
