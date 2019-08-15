@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    plain_msg = (EditText)findViewById(R.id.msg2encypt);
    pwd = (EditText)findViewById(R.id.password);
    result = (TextView)findViewById(R.id.decrypttxt);
}

public void mybuttonHandler(View view){
    String S_plain_msg = plain_msg.getText().toString();
    String S_pwd = pwd.getText().toString();
    setAES(S_plain_msg, S_pwd);
}


private byte[] generateSalt() throws NoSuchAlgorithmException{
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    byte[] ransalt = new byte[20];
    random.nextBytes(ransalt);
    return ransalt;
}


private void setAES(String msg, String pwd){
    try {
        //Generation of Key
        byte[] salt = generateSalt();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
        KeySpec spec = new PBEKeySpec(pwd.toCharArray(),salt,1024, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        //Encryption process
        byte[] btxt = Base64.decode(msg, 0);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret); 
        AlgorithmParameters params = cipher.getParameters(); 
        iv = params.getParameterSpec(IvParameterSpec.class).getIV(); 
        byte[] ciphertext = cipher.doFinal(btxt);
        String encryptedtext = Base64.encodeToString(ciphertext, 0);

        //Decryption process
        byte[] bencryptxt = Base64.decode(encryptedtext, 0);
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv)); 
        ciphertext = cipher.doFinal(bencryptxt);
        String cipherS = Base64.encodeToString(ciphertext, 0); 

        result.setText(cipherS);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
    } 

}
