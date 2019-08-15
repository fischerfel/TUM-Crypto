     public void generateKey() throws NoSuchAlgorithmException, NoSuchPaddingException,
                IllegalBlockSizeException,BadPaddingException,InvalidKeyException{
            try{
                kpg = KeyPairGenerator.getInstance("RSA");

                kpg.initialize(256);
                kp = kpg.genKeyPair();

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getBaseContext(), e.toString(),Toast.LENGTH_LONG).show();

            }

        }
        public byte[] RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
                IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        try {
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();

            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedBytes = cipher.doFinal(plain.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
            return encryptedBytes;
        }
 public void enviaSMS(View view) {
        EditText key = (EditText) findViewById(R.id.publicKey);
        EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        EditText text = (EditText) findViewById(R.id.TextMessage);
        String keyText = key.getText().toString();
        String number = phoneNumber.getText().toString();
        String sms = text.getText().toString();
        if (!keyText.equals("") && !number.equals("") && !sms.equals("")) {
            try {

                byte[] encriptedSMS= RSAEncrypt(sms);
                Log.i("teste",new String(encriptedSMS));
                Log.i("teste",new String(encriptedSMS, "UTF-8"));
                Toast.makeText(getBaseContext(), new String(encriptedSMS, "UTF-8"), Toast.LENGTH_SHORT).show(); // ou send?:3
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(number, null, new String(encriptedSMS,"UTF-8"), null, null);

                Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

        }
    }
