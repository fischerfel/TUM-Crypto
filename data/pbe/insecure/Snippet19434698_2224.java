public class MainActivity extends Activity implements OnClickListener {

EditText ed1, ed2, ed3;
private final char[] PASSWORD = "abcdefghijklmnop".toCharArray();
private byte[] SALT;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

   //Setting the SALT to android_id
    SALT = Secure.getString(getContentResolver(), Secure.ANDROID_ID).getBytes();
    Button btn = (Button) findViewById(R.id.button1);
    btn.setOnClickListener(this);
    ed1 = (EditText) findViewById(R.id.editText1);
    ed2 = (EditText) findViewById(R.id.editText2);
    ed3 = (EditText) findViewById(R.id.editText3);
}

public void onClick(View v)
{

    String encrypted, decrypted, userpass = ed1.getText().toString().trim();
    encrypted = encrypt(userpass);
    ed2.setText(encrypted);
    decrypted = decrypt(encrypted);
    ed3.setText(decrypted);
}

public String encrypt(String original)
{

    SecretKeyFactory secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = secretKey.generateSecret(new PBEKeySpec(PASSWORD));
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT,20));
    String encrypted = cipher.doFinal(original.getBytes("UTF-8")).toString();
    return encrypted;
}

public String decrypt(String original)
{

    SecretKeyFactory secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = secretKey.generateSecret(new PBEKeySpec(PASSWORD));
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT,10));
    String decrypted = cipher.doFinal(original.getBytes("UTF-8")).toString();
    return decrypted;
  }
}
