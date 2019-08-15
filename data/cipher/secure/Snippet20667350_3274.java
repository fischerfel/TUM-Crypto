// AESdemo

public class AESdemo extends Activity {
    boolean encryptionIsOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aesdemo);
        // needs <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        String homeDirName = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + getPackageName();
        File file = new File(homeDirName, "test.txt");
        byte[] keyBytes = getKey("password");

        try {
            File dir = new File(homeDirName);
            if (!dir.exists())
                dir.mkdirs();
            if (!file.exists())
                file.createNewFile();

            OutputStreamWriter osw;

            if (encryptionIsOn) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileOutputStream fos = new FileOutputStream(file);
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                osw = new OutputStreamWriter(cos, "UTF-8");
            }
            else    // not encryptionIsOn
                osw = new FileWriter(file);

            BufferedWriter out = new BufferedWriter(osw);
            out.write("This is a test\n");
            out.close();
        }
        catch (Exception e) {
            System.out.println("Encryption Exception "+e);
        }

        ///////////////////////////////////
        try {
            InputStreamReader isr;

            if (encryptionIsOn) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

                FileInputStream fis = new FileInputStream(file);
                CipherInputStream cis = new CipherInputStream(fis, cipher);
                isr = new InputStreamReader(cis, "UTF-8");
            }
            else
                isr = new FileReader(file);

            BufferedReader in = new BufferedReader(isr);
            String line = in.readLine();
            System.out.println("Text read: <"+line+">");
            in.close();
        }
        catch (Exception e) {
            System.out.println("Decryption Exception "+e);
        }
    }

    private byte[] getKey(String password) throws UnsupportedEncodingException {
        String key = "";
        while (key.length() < 16)
            key += password;
        return key.substring(0, 16).getBytes("UTF-8");
    }
}
