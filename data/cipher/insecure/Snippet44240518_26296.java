public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
private static final String TAG = "DESA" ;
private ZXingScannerView mScannerView;

String m_Text="";
private Button butto;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    butto = (Button) findViewById(R.id.button);
    butto.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            QrScanner(view);
        }
    });
}

public void QrScanner(View view){


    mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
    setContentView(mScannerView);

    mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
    mScannerView.startCamera();         // Start camera

}

@Override
public void onPause() {
    super.onPause();
    mScannerView.stopCamera();           // Stop camera on pause
}
private static String cryptoPass = "sup3rS3xy";

public static String decryptIt(String value) {
    try {
        DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
        // cipher is not thread safe
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

        String decrypedValue = new String(decrypedValueBytes);
        Log.d(TAG, "Decrypted: " + value + " -> " + decrypedValue);
        return decrypedValue;

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return value;
}

@Override
public void handleResult(Result rawResult) {

    final Context context = this;
    // Do something with the result here
    String s = rawResult.getText().toString();
    final String pwd;
    s = decryptIt(s);
    int len = s.length();
    final String newStr = s.substring(0,(len-5));
    pwd = s.substring((len-5),len);

    Log.e("handler", s); // Prints scan results
    Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Enter QR PIN");
    final EditText input = new EditText(this);
    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    builder.setView(input);
    final AlertDialog.Builder builder1 = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            m_Text = input.getText().toString();
            if (m_Text.equals(pwd)) {
                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("result", newStr);
                startActivity(intent);
            }
            else
            {
                Intent intent3 = new Intent(context,MainActivity.class);
                startActivity(intent3);
            }
        }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            Intent intent2 = new Intent(context,MainActivity.class);
            startActivity(intent2);

        }
    });

    builder.show();
}
}
