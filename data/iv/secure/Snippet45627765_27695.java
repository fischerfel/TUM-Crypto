public class MainActivity extends AppCompatActivity {
private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
private final static int IV_LENGTH = 16;
Cursor mVideoCursor;
ArrayList<HashMap<String, String>> listOfVideo;
@Override    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    listOfVideo = new ArrayList();
    String[] videoColumns = {MediaStore.Video.Media.DATA,MediaStore.Video.Media.DURATION,
    MediaStore.Video.Media.SIZE,MediaStore.Video.Media.DISPLAY_NAME};
    mVideoCursor = getApplicationContext().getContentResolver().query
            (MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
    mVideoCursor.moveToFirst();
    for (int i = 0; i < mVideoCursor.getCount(); i++) {
        listOfVideo.add(new HashMap<String, String>() {
            {
                put("data", String.valueOf(mVideoCursor.getString( 
                mVideoCursor.getColumnIndex(MediaStore.Video.Media.DATA))));
                put("duration", String.valueOf(mVideoCursor.getString(
                mVideoCursor.getColumnIndex(MediaStore.Video.Media.DURATION))));
                put("displayName", String.valueOf(mVideoCursor.getString(
                mVideoCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))));
                put("size", String.valueOf(mVideoCursor.getString(
                mVideoCursor.getColumnIndex(MediaStore.Video.Media.SIZE))));
                mVideoCursor.moveToNext();

            }
        });
    }

    String path = listOfVideo.get(0).get("data");
    File inFile = new File(listOfVideo.get(0).get("data"));
    File outFile = new File(path.substring(0, path.lastIndexOf("/"))+"/enc_video.swf");
    File outFile_dec = new File(path.substring(0, path.lastIndexOf("/"))+"/dec_video.mp4");

    try {
        SecretKey key = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();
        byte[] keyData = key.getEncoded();
        SecretKey key2 = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR);  
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        Encrypter.encrypt(key, paramSpec, 
        new FileInputStream(inFile), new FileOutputStream(outFile));
        Encrypter.decrypt(key2, paramSpec, 
        new FileInputStream(outFile), new FileOutputStream(outFile_dec));
    } catch (Exception e) {
        e.printStackTrace();
    }

}

}
