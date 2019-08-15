public class MainActivity extends AppCompatActivity {
    KeyPairGenerator kpg;
    KeyPair kp;
    PublicKey publicKey;
    PrivateKey privateKey;
    byte[] encryptedBytes, decryptedBytes;
    Cipher cipher, cipher1;
    String encrypted, decrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            generateKey();
            TextView txtPuk = (TextView) findViewById(R.id.tViewPUK);
            txtPuk.setText(kp.getPublic().toString());
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.toString(),Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void generateKey() throws NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException,BadPaddingException,InvalidKeyException{
        try{
                kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024); //NULLPOINTER HERE
            kp = kpg.genKeyPair();

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.toString(),Toast.LENGTH_LONG).show();

        }

    }
    public byte[] RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        ;
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());
        return encryptedBytes;
    }

    public String RSADecrypt(final byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(encryptedBytes);
        decrypted = new String(decryptedBytes);
        return decrypted;
    }

}
