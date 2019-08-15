Button GenKey = null;
Button encryptString = null;
Button decryptString = null;
EditText plainField = null;
EditText encryptedField = null;

@Override
public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    GenKey = (Button) findViewById(R.id.btnGenKey);
    GenKey.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
                generateKey();
        }
    });

    encryptString = (Button) findViewById(R.id.btnEncrypt);
    encryptString.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            byte[] encryptedData = null;
            byte[] dataToEncrypt = null;
            plainField = (EditText) findViewById (R.id.eTxtPlainTxt);
            String plainText = plainField.getText().toString();
                dataToEncrypt = plainText.getBytes("UTF-8");
                encryptedData = rsaEncrypt(dataToEncrypt);              
            String encryptedText=null;
                encryptedText = new String(encryptedData, "UTF-8");
            encryptedField = (EditText)findViewById(R.id.eTxtCiphTxt);
            encryptedField.setText(encryptedText);
        }
    });

    decryptString = (Button) findViewById(R.id.btnDecrypt);
    decryptString.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            byte[] decryptedData = null;
            byte[] dataToEncrypt = null;
            encryptedField = (EditText) findViewById (R.id.eTxtCiphTxt);
            String cipherText = encryptedField.getText().toString();    
                dataToEncrypt = cipherText.getBytes("UTF-8");
                decryptedData = rsaDecrypt(dataToEncrypt);

            String decryptedText = null;
                decryptedText = new String(decryptedData,"UTF-8");
            plainField = (EditText)findViewById(R.id.eTxtPlainTxt);
            plainField.setText(decryptedText);
        }
    });

}//end of onCreate

public void generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(512);
    KeyPair kp = kpg.genKeyPair();

    KeyFactory fact = KeyFactory.getInstance("RSA");
    RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
    RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

    saveToFile("public.key", pub.getModulus(),pub.getPublicExponent());
    saveToFile("private.key", priv.getModulus(),priv.getPrivateExponent());
    }//end of generateKey

public void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {        
    FileOutputStream fOutStream = openFileOutput(fileName, MODE_PRIVATE);
    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fOutStream));
    oos.writeObject(mod);
    oos.writeObject(exp);
    oos.flush();
    oos.close();
    }//end of saveToFile

public PublicKey readPubKeyFromFile(String keyFileName) throws StreamCorruptedException, IOException{
    FileInputStream fInStream = openFileInput(keyFileName);
    ObjectInputStream oInStream = new ObjectInputStream(new BufferedInputStream(fInStream));
        BigInteger m = (BigInteger) oInStream.readObject();
        BigInteger e = (BigInteger) oInStream.readObject();
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(keySpec);
        return pubKey;
        oInStream.close();
}//end of readPubKeyFromFile

public PrivateKey readPrivKeyFromFile(String keyFileName) throws StreamCorruptedException, IOException{
    FileInputStream fInStream = openFileInput(keyFileName);
    ObjectInputStream oInStream = new ObjectInputStream(new BufferedInputStream(fInStream));
        BigInteger m = (BigInteger) oInStream.readObject();
        BigInteger e = (BigInteger) oInStream.readObject();
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privKey = fact.generatePrivate(keySpec);
        return privKey;
        oInStream.close();
}//end of readPubKeyFromFile

public byte[] rsaEncrypt(byte[] data) throws StreamCorruptedException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    PublicKey pubKey = readPubKeyFromFile("public.key");
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] cipherData = cipher.doFinal(data);
    return cipherData;    
}//end of rsaEncrypt

public byte[] rsaDecrypt(byte[] data) throws StreamCorruptedException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    PrivateKey privKey = readPrivKeyFromFile("private.key");
    Cipher decipher = Cipher.getInstance("RSA");
    decipher.init(Cipher.DECRYPT_MODE, privKey);
    byte[] plainData = decipher.doFinal(data);
    return plainData;   
}
