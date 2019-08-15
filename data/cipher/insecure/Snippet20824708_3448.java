   public class EncryptionTest extends Activity {

EditText input, output, outputDecrypt;
String plain_text;
byte[] key, encrypted_bytes,keyStart,byte_char_text,decrpyted_bytes ;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_encryption_test);

    input = (EditText) findViewById(R.id.text_inputText);
    output = (EditText) findViewById(R.id.text_Result);
    outputDecrypt = (EditText) findViewById(R.id.text_decrypt_Result);
    Button encrypt_btn = (Button) findViewById(R.id.btn_encrpyt);
    Button decrypt_btn = (Button) findViewById(R.id.btn_Decrypt);

    plain_text = input.getText().toString();
    keyStart = "Supriyo".getBytes();
    byte_char_text = plain_text.getBytes();

    encrypt_btn.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {


            try {

            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed(keyStart);
                keygen.init(128, sr);
                SecretKey skey = keygen.generateKey();
                key = skey.getEncoded();

                encrypted_bytes = encrypt(key, byte_char_text);
                String inputResult = encrypted_bytes.toString();
                output.setText(inputResult);
        decrpyted_bytes = decrypt(key, encrypted_bytes);
                     System.out.println("decr"+Arrays.toString(decrpyted_bytes));                                               
            String outputResult = new String(decrpyted_bytes,"UTF-8");
                System.out.println("-->>>"+outputResult);
                outputDecrypt.setText(outputResult);

            } catch (NoSuchAlgorithmException e) {

                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    });
        }

public static byte[] decrypt(byte[] raw, byte[] encrypteds)
        throws Exception          {

    SecretKeySpec skey = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skey);
    byte[] decrypted = cipher.doFinal(encrypteds);
    return decrypted;
}

public static byte[] encrypt(byte[] raw, byte[] clear)
        throws Exception{

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte encrypted[] = cipher.doFinal(clear);

    return encrypted;
}

     @Override
      public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.encryption_test, menu);
    return true;
}

    }
