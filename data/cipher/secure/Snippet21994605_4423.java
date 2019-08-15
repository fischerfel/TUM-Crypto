    @Override
    public void onClick(View arg0) {
// TODO Auto-generated method stub
        FILENAME = filename.getText().toString();
        PASSWORD = pass.getText().toString();

        // Generate key pair for 1024-bit RSA encryption and decryption
        Key publicKey = null;
        Key privateKey = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (Exception e) {
            Log.e(TAG, "RSA key pair error");
        }

        // Encode the original data with RSA private key
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedBytes = c.doFinal(PASSWORD.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "RSA encryption error");
        }

        PASSWORD = Base64.encodeToString(encodedBytes, Base64.DEFAULT);

        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            fos.write(PASSWORD.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(),
                    "Your Credentials are saved.", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
