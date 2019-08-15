    public class MainActivity extends Activity {

//@SuppressWarnings("unchecked")
private static final String TAG = "MyActivity";
@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);       

     AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input 
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new
      DialogInterface.OnClickListener()   {
        public void onClick(DialogInterface dialog, int whichButton) {
        String value = input.getText().toString();
        try {
                encrypt(value);
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }});

        alert.setNegativeButton("Cancel", new
                   DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int
          whichButton) {
                 // Canceled.
            }
            });

             alert.show();  }               
    static void encrypt(String Value) throws IOException,
       NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

    final String libPath = Environment.getExternalStorageDirectory() + "/shoaib.jar";
    // Here you read the cleartext.
    FileInputStream fis = new FileInputStream(libPath);
    // This stream write the encrypted text. 
    FileOutputStream fos = new 
    FileOutputStream(Environment.getExternalStorageDirectory() + "/encrypted.jar");

    // Length is 16 byte
    SecretKeySpec sks = new SecretKeySpec(Value.getBytes(), "AES");
    Log.d(TAG,Value);
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    // Wrap the output stream
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    // Write bytes
    int b;
    byte[] d = new byte[8];
    while((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }
    // Flush and close streams.
    cos.flush();
    cos.close();
    fis.close();
    }
