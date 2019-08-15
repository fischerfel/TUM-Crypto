public class MainActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_aeshelper);

    Button btn = (Button) findViewById(R.id.button1);

    final String FileDir = "data/";


    btn.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            try {
                FileInputStream fis = new FileInputStream(new File(FileDir+"aaa.mp4"));
                File outfile = new File(FileDir + "EncryptedVideo.mp4");
                int read;
                if (!outfile.exists())
                    outfile.createNewFile();
                File decfile = new File(FileDir + "DecryptedVideo.mp4");
                if (!decfile.exists())
                    decfile.createNewFile();
                FileOutputStream fos = new FileOutputStream(outfile);
                FileInputStream encfis = new FileInputStream(outfile);
                FileOutputStream decfos = new FileOutputStream(decfile);
                Cipher encipher = Cipher.getInstance("AES");
                Cipher decipher = Cipher.getInstance("AES");
                KeyGenerator kgen = KeyGenerator.getInstance("AES");

                SecretKey skey = kgen.generateKey();
                encipher.init(Cipher.ENCRYPT_MODE, skey);
                CipherInputStream cis = new CipherInputStream(fis, encipher);
                decipher.init(Cipher.DECRYPT_MODE, skey);
                CipherOutputStream cos = new CipherOutputStream(decfos, decipher);
                while ((read = cis.read()) != -1) {
                    fos.write((char) read);
                    fos.flush();
                }
                fos.close();
                while ((read = encfis.read()) != -1) {
                    cos.write(read);
                    cos.flush();
                }
                cos.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"!!!", 
                           Toast.LENGTH_LONG).show();               }
        }

    });




}   
