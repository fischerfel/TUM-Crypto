public class MainActivity extends Activity {

    private static final String TAG = "MD5";
    //private TextView textView_hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        final EditText editText1 = (EditText)findViewById(R.id.editText1);
        final TextView textView3 = (TextView)findViewById(R.id.textView3);
        final String   EditText1 = editText1.getText().toString();


        button1.setOnClickListener(new OnClickListener()
        {


            @Override
            public void onClick(View v)
            {
               hitungMD5(EditText1);
            }
        });

        button2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editText1.setText("");
                textView3.setText("");
            }
        });

    }

    public void hitungMD5(String editText1) {
        // TODO Auto-generated method stub

        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(editText1.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                MD5Hash.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            TextView textView3 = null;
            textView3.setText(MD5Hash);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
