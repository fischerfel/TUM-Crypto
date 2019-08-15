   @Override
        public void onCreate() {
            super.onCreate();

            secretKey = null;
            Log.e(TAG, "Build.SERIAL = " + Build.SERIAL);

            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = secureRandom.generateSeed(256);


            try {
                secretKey = generateKey(Build.SERIAL.toCharArray(), salt);
                 Log.e(TAG, "key-Base64 before in appObj = "+new String(Base64.encode(RROnCallApplication.getSecretKey().getEncoded(),0)));
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.e(TAG, "secretKey = " + secretKey);

            SQLiteDatabase.loadLibs(this);

            dbModel = new DBModel(this);
            webService = new WebService(this);
            alertCount = 0;

    //      Cursor checkCarerTable = dbModel.queryAllFromCarer();
    //      
    //      if(checkCarerTable.getCount() == 0){
    //      
    //      //runGetCarersService();
    //      //runGetClientsService();
    //      
    //      
    //      }else{
    //          
    //          Log.e(TAG, "carer and client table is populated with some data"); 
    //      }



        }

private static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    // Number of PBKDF2 hardening rounds to use. Larger values increase
    // computation time. You should select a value that causes computation
    // to take >100ms.
    final int iterations = 1000; 

    // Generate a 256-bit key
    final int outputKeyLength = 256;

    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

    return secretKey;
}
