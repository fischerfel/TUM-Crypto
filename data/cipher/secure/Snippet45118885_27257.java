FileInputStream keyFis = new FileInputStream(encKeyFile);
byte[] encKey = new byte[keyFis.available()];
keyFis.read(encKey);
keyFis.close();

SecretKey key = null;
PrivateKey privKey = readRsaPrivateKeyFromResource(context);
Cipher cipher = null;

try
{
    // initialize the cipher...
    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privKey);
    // generate the aes key!
    key = new SecretKeySpec (cipher.doFinal(encKey), "AES" );
    String stringKey = Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    try {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("aesDecrypted.key", Context.MODE_PRIVATE));
        outputStreamWriter.write(stringKey);
        outputStreamWriter.close();
    }
    catch (IOException e) {
        Log.e("Exception", "File write failed: " + e.toString());
    }
