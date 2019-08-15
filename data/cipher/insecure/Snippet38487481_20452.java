public class EncryptActivity extends AppCompatActivity {

EditText ed1;
Button b1;

private ClipboardManager myClipboard;
private ClipData myClip;
KeyGenerator keygenerator;
static SecretKey secretkey;

Cipher cipher;
byte[] encrypted;
String encryptedText = null;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.encrypt_activity);
    ed1 = (EditText) findViewById(R.id.editText);
    b1 = (Button) findViewById(R.id.button);
    myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = ed1.getText().toString();//now encrypt the message
            try {
                keygenerator = KeyGenerator.getInstance("Blowfish");
                secretkey = keygenerator.generateKey();
                Log.d("asd", secretkey.toString());
                cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.ENCRYPT_MODE, secretkey);
                encrypted = cipher.doFinal(text.getBytes());
                encryptedText = encrypted.toString();
                myClip = ClipData.newPlainText("text", encryptedText);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    });
}
