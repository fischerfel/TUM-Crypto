public class MainActivity extends Activity {

Button button1, button2;

String KEY = "MyKey";
SparseArray<byte[]> array = new SparseArray<byte[]>();
SparseArray<byte[]> decryptArray = new SparseArray<byte[]>();

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    button1 = (Button) findViewById(R.id.button1);
    button1.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
           try {
               new EncryptAsyncTask().execute();
           } catch (Exception e) {  
               e.printStackTrace();
           }
        }
    });

    button2 = (Button) findViewById(R.id.button2);
    button2.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                new DecryptAsyncTask().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });


   public class EncryptAsyncTask extends AsyncTask<String, String, String>{

   ProgressDialog mDialog;

   @Override
   protected void onPreExecute() {
        super.onPreExecute();
        mDialog = ProgressDialog.show(MainActivity.this, "", "Please wait");
   }

    @Override
    protected String doInBackground(String... params) {
         byte[] incrept = null;
        try {
            getImageFile();
            if(array!=null && array.size()>0){
                for(int i=0 ; i<array.size() ; i++){
                    byte[] byteArray = array.get(i);
                    incrept = encrypt(KEY, byteArray);
                     FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(new File(
                                   Environment.getExternalStorageDirectory()+File.separator
                                    +"EncryptedImages"+File.separator+i+"_Image.jpg"));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(incrept);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }               
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
 }

 public class DecryptAsyncTask extends AsyncTask<String, String, String>{

   ProgressDialog mDialog;

   @Override
protected void onPreExecute() {
super.onPreExecute();
    mDialog = ProgressDialog.show(MainActivity.this, "", "Please wait");
}

    @Override
    protected String doInBackground(String... params) {
         byte[] incrept = null;
        try {
            getImageFileFromSdCard();
            if(decryptArray!=null && decryptArray.size()>0){
                for(int i=0 ; i<decryptArray.size() ; i++){
                    byte[] byteArray = decryptArray.get(i);
                    incrept = decrypt(KEY, byteArray);                        
                     FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(new File(
                                   Environment.getExternalStorageDirectory()+File.separator
                                    +"DecryptedImages"+File.separator+i+"_Image.jpg"));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(incrept);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }               
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[]   getImageFile() throws FileNotFoundException
{
  byte[] Image_data = null;
  byte[] arry = null;
    try {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"Images"+File.separator);
        if(file.exists() && file.isDirectory()){
            File[] listOfImages = file.listFiles();
            if(listOfImages.length>0){
                 for(int i=0; i<listOfImages.length; i++){
                     byte[] inarry = null;
                     InputStream is = new BufferedInputStream(new FileInputStream(listOfImages[i]));
                     int length = is.available();
                        Image_data = new byte[length];

                        int bytesRead;
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        while ((bytesRead = is.read(Image_data)) != -1)
                        {
                            output.write(Image_data, 0, bytesRead);
                        }
                      inarry = output.toByteArray();
                      array.put(i, inarry);
                      is.close();
                 }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

return arry;
}

public void getImageFileFromSdCard() throws FileNotFoundException
{
try {
    File file = new            File(Environment.getExternalStorageDirectory()+File.separator+"EncryptedImages"+File.separator);
    if(file.exists() && file.isDirectory()){
        File[] listOfFiles = file.listFiles();
        if(listOfFiles.length>0){
            for(int i=0 ; i<listOfFiles.length ; i++){
                FileInputStream fileInputStream = new FileInputStream(listOfFiles[i]);
                byte[] bFile = new byte[(int) listOfFiles[i].length()];
                fileInputStream.read(bFile);
                fileInputStream.close();
                decryptArray.put(i, bFile);
            }
        }
    }
} catch (IOException e) {
    e.printStackTrace();
}
}

public  byte[] encrypt(String seed, byte[] cleartext) throws Exception {

    byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext);
        return result;
}

public  byte[] decrypt(String seed, byte[] encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = encrypted;
        byte[] result = decrypt(rawKey, enc);

        return result;
}

private  byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
    kgen.init(128, sr); 
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
} 


private  byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.update(clear);
        return encrypted;
}

private  byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] decrypted = cipher.update(encrypted);
        return decrypted;
}
 }
