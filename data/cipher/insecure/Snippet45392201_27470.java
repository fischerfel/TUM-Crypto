public class MainActivity extends AppCompatActivity {
TextView txtChuoi, txtKey, txtMaHoa, txtGiaiMa;
Button btnMaHoa, btnGiaiMa;
private static Cipher ecipher;
private static Cipher dcipher;
private  static SecretKey key;
String encrypted;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    addControl();
    addEvent();
}

private void addEvent() {
    btnMaHoa.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {

                key = KeyGenerator.getInstance("DES").generateKey();
                /////How to use
                ////txtKey.setText().toString() -----> for SecretKey?
                ecipher = Cipher.getInstance("DES");
                ecipher.init(Cipher.ENCRYPT_MODE,key);
                encrypted = encrypt(txtChuoi.getText().toString());
                txtMaHoa.setText(encrypted);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    });
    btnGiaiMa.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String decrypt = decrypt(encrypted);
            txtGiaiMa.setText(decrypt);
        }
    });
}

private String decrypt(String encrypted) {

    try {
        dcipher = Cipher.getInstance("DES");
        dcipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dec = Base64.decode(encrypted.getBytes(),Base64.DEFAULT);
        byte[] utf8 = dcipher.doFinal(dec);
        return  new String(utf8,"UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    } 
    return null;
}

private String encrypt(String s){
    byte[]enc = null;
    try {
        byte[] utf8 = s.getBytes("UTF-8");
        enc = ecipher.doFinal(utf8);
        enc = Base64.encode(enc,Base64.DEFAULT);
        return new String(enc);
    } catch (Exception e) {
        e.printStackTrace();
    } 
    return null;
}


private void addControl() {
    txtChuoi = (TextView) findViewById(R.id.txtChuoi);
    txtKey = (TextView) findViewById(R.id.txtKey);
    txtMaHoa = (TextView) findViewById(R.id.txtMaHoa);
    txtGiaiMa = (TextView) findViewById(R.id.txtGiaiMa);
    btnMaHoa = (Button) findViewById(R.id.btnMaHoa);
    btnGiaiMa = (Button) findViewById(R.id.btnGiaiMa);

    }
}
