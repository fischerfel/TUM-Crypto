byte[] key = "1428324560542678".getBytes();

Cipher c = null;
            try {
                c = Cipher.getInstance("AES/ECB/PKCS7Padding");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

SecretKeySpec k =  new SecretKeySpec(key, "AES");
            try {
                c.init(Cipher.ENCRYPT_MODE, k);
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    try {
        EditText tv1passwordText = (EditText) findViewById(R.id.password);
        String password = URLEncoder.encode(tv1passwordText.getText().toString(), "UTF-8");

            byte[] encryptedData = c.doFinal( password.getBytes());
