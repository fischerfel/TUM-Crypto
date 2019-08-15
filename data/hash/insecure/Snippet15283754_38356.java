public class MainActivity extends Activity {

    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText text = (EditText) findViewById(R.id.editText1);
                EditText text1 = (EditText) findViewById(R.id.editText2);
                String userid = text.getText().toString();
                String pass = text1.getText().toString();

                Toast.makeText(
                        MainActivity.this,
                        "Entered " + userid + " and password entered is "
                                + pass, Toast.LENGTH_LONG).show();

                pass = md5(pass + "@string/salt");

                Toast.makeText(
                        MainActivity.this,
                        "Password after adding a salt and md5 hashing is now equal to "
                                + pass, Toast.LENGTH_LONG).show();

                /* This Line */DBAdapter db = new DBAdapter(this); // This Line.
                db.open();
                Cursor c = db.getPasswordForUserName(userid);
                if (c.moveToFirst()) {
                    if (c.getString(1) == pass) {
                        Toast.makeText(MainActivity.this,
                                "Authentication Succeded", Toast.LENGTH_SHORT)
                                .show();
                        // proceed
                    } else {
                        Toast.makeText(MainActivity.this, "@string/AuthFail",
                                Toast.LENGTH_SHORT).show();
                        // AuthFailure
                    }
                } else {
                    Toast.makeText(MainActivity.this, "@string/UserNotFound",
                            Toast.LENGTH_SHORT).show();
                    // where to from here
                }
            }
        });
    }
}
