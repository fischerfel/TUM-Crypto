public class MainActivity extends Activity {

  EditText encryptText;
  Button encryptButton;
  String key = "16bitkey";
  String requestData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  public void initViews(){

    encryptText = (EditText) findViewById(R.id.encryptText);
    encryptButton = (Button) findViewById(R.id.encryptButton);
    encryptButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        new event_background().execute();
      }
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  public void performSocketRequest(){
    try{
      requestData = encryptText.getText().toString();
      byte[] keyInBytes = convertToByteArray(key);
      byte[] requestInBytes = convertToByteArray(requestData);
      byte[] aesEncryptedData = aesEncryption(keyInBytes, requestInBytes);
    }
    catch(Exception e){
    }
  }

  public byte[] convertToByteArray(String data){

    byte[] bytes = null;
    try{
      bytes = data.getBytes("UTF8");
    }catch(Exception e){

    }
    return bytes;
  }

  public byte[] aesEncryption(byte[] raw, byte[] clear) throws Exception{

    try{

      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      sr.setSeed(raw);
      kgen.init(128, sr);
      SecretKey skey = kgen.generateKey();
      byte[] keyTemp = skey.getEncoded(); 
      byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      SecretKeySpec skeySpec = new SecretKeySpec(keyTemp, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
      return cipher.doFinal(clear);

    }catch(IllegalBlockSizeException e){
      Log.e("*********** IllegalBlockSizeException error **************", e.getMessage());
    }catch(BadPaddingException e){          
      Log.e("*********** BadPaddingException error **************", e.getMessage());          
    }catch(Exception e){          
      Log.e("*********** error **************", e.getMessage());
    }

    return null;
  }

  class event_background extends AsyncTask<Void, String, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void unused) {
    }

    @Override
    protected Void doInBackground(Void... params) {
      // TODO Auto-generated method stub
      performSocketRequest();
      return null;
    }
  }
}
