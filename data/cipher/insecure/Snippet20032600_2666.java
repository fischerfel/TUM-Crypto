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
                Decrypt(value);
                Log.d(TAG,value);
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

             alert.show();  
    }
    static void Decrypt(String Key) throws IOException, NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException {

    final String libPath = Environment.getExternalStorageDirectory() +  
    "/encrypted.jar";
    FileInputStream fis = new FileInputStream(libPath);

    FileOutputStream fos = new  
    FileOutputStream(Environment.getExternalStorageDirectory() + "/decrypted.jar");
    SecretKeySpec sks = new SecretKeySpec(Key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks /*originalKey*/);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while((b = cis.read(d)) != -1) {
    fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
    }
