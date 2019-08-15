private static byte[] keyValue = new byte[]{ 'W', 'e', 'l', 'c', 'o', 'm', 'e','t', 'o', 'e', 'n','c', 'r', 'y', 'p', 't' };

private String seedWith16Chars = new String(keyValue);
private String textToEncrypt = "1";

private TextView seed;
private TextView text;
private TextView encryptedValue;
private TextView decryptedValue;


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    seed = (TextView) findViewById(R.id.seedName);
    seed.setText(seedWith16Chars);

    text = (TextView) findViewById(R.id.textToEncrypt);
    text.setText(textToEncrypt);

    encryptedValue = (TextView) findViewById(R.id.encryptedText);
    decryptedValue = (TextView) findViewById(R.id.decryptedText);

    try {
        // This value was got when did run it from an 2.3.3 device a Galaxy SII running Android 4.0.4
        String encrypted = "";

        // Uncomment the line bellow and comment the line above to run it on an Android 4.1.2 or older.
        // String encrypted = EncodeDecodeAES.encrypt(seedWith16Chars, textToEncrypt);
        Log.e("Encrypt", encrypted);
        encrypted = encrypt(textToEncrypt);//EncodeDecodeAES.encrypt(seedWith16Chars, textToEncrypt);
        encryptedValue.setText("Encrypt "+encrypted);

        String decrypted = decrypt(encrypted);//EncodeDecodeAES.decrypt(seedWith16Chars, encrypted);
        decryptedValue.setText("Decrypt "+decrypted);
        Log.e("Decrypt", decrypted);
    } catch (Exception e) {
        Log.e("Exception", e.getLocalizedMessage());
    }

}

public static String encrypt(String Data) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    String encryptedValue = Base64.encodeToString(encVal, 0);
    return encryptedValue;
}

public static String decrypt(String encryptedData) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = Base64.decode(encryptedData, 0);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue;
}
private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, "AES");
    return key;
}
